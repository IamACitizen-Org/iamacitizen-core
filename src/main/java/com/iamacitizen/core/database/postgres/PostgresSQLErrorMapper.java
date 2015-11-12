package com.iamacitizen.core.database.postgres;

import com.iamacitizen.core.database.AbstractDatabase;
import com.iamacitizen.core.database.AbstractSQLErrorMapper;
import com.iamacitizen.core.database.DatabaseConstants;
import com.iamacitizen.core.exception.SerigyDatabaseException;
import com.iamacitizen.core.exception.SerigyException;
import com.iamacitizen.core.model.annotation.AnnotationDescriptor;
import com.iamacitizen.core.model.annotation.AnnotationDescriptorFinder;
import com.iamacitizen.core.model.database.Constraint;
import com.iamacitizen.core.model.database.ConstraintType;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostgresSQLErrorMapper extends AbstractSQLErrorMapper {

	public PostgresSQLErrorMapper(AbstractDatabase database) {
		super(database);
		addMessage("1", "Já existe um registro em %table% com o mesmo valor para: %value%");
		addMessage("1B", DatabaseConstants.DUPLICATED_RECORD);
		addMessage("1400", "Valor do campo %value% não pode ser nulo");
		addMessage("1400B", DatabaseConstants.VALUE_REQUIRED);
		addMessage("12899", "Tamanho do campo %value% excede o tamanho máximo de %max% caracteres");
		addMessage("12899B", DatabaseConstants.VALUE_TOO_BIG);
		addMessage("2291", "Não existe um registro em %value% correspondente ao valor informado");
		addMessage("2291B", DatabaseConstants.PARENT_KEY_NOT_FOUND);
		addMessage("2292", "Registro excluído é referenciado em %value%");
		addMessage("2292B", DatabaseConstants.CHILD_FOUND);
		addMessage("DEFAULT", "Ocorreu um erro inesperado de banco de dados.\n");
	}

	@Override
	public String SQLErrorHandler(SQLException e) {

		String message = this.map(String.valueOf(e.getErrorCode()));

		switch (e.getErrorCode()) {
			case 0001: {
				message = primaryKeyViolation(message, e.getMessage());
				break;
			}
			case 1400: {
				message = nullValueViolation(message, e.getMessage());
				break;
			}
			case 12899: {
				message = fieldValueTooBig(message, e.getMessage());
				break;
			}
			case 2291: {
				message = parentKeyViolation(message, e.getMessage());
				break;
			}
			case 2292: {
				message = childRecordViolation(message, e.getMessage());
				break;
			}
			default:
				message = this.map("DEFAULT") + e.getMessage();
		}
		return message;
	}

	//<editor-fold defaultstate="collapsed" desc="Violações">
	private String primaryKeyViolation(String message, String initialMessage) {
		String result = "";
		String regex = "\\(\\w+\\.\\w+\\)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(initialMessage);

		String owner = "";
		String name = "";
		if (matcher.find()) {
			String group = matcher.group();
			owner = group.substring(1, group.indexOf("."));
			name = group.substring(group.indexOf(".") + 1, group.indexOf(")"));
		}

		Constraint c = getDatabase().getConstraint(owner, name);

		if (c != null) {
			try {
				String alias = "";
				String table = "";

				AnnotationDescriptor descriptor = null;

				if (c.getType().equals(ConstraintType.PRIMARY_KEY)) {
					descriptor = AnnotationDescriptorFinder.findDescriptor(c.getTable());

					if (descriptor != null) {
						alias = descriptor.getPrimaryKeyAlias();
						table = descriptor.getTableAlias();
					}

				} else {
					descriptor = AnnotationDescriptorFinder.findDescriptor(c.getIndexedColumn());

					if (descriptor != null) {
						alias = descriptor.getAliasFromColumnName(c.getIndexedColumn());
					}
				}

				if (!"".equals(alias) && !"".equals(table)) {
					result = message.replace("%value%", alias).replace("%table%", table);
				} else {
					result = map("1B");
				}

			} catch (SerigyException ex) {
				result = map("1B");
			}
		} else {
			result = map("1B");
		}
		return result;
	}

	private String nullValueViolation(String message, String initialMessage) {
		String result = "";
		try {
			int index = initialMessage.lastIndexOf("\".") + 3;
			String columnName = initialMessage.substring(index, initialMessage.length() - 3);
			AnnotationDescriptor descriptor = AnnotationDescriptorFinder.findDescriptor(columnName);

			String alias = "";
			if (descriptor != null) {
				alias = descriptor.getAliasFromColumnName(columnName);
			}

			if (!"".equals(alias)) {
				result = message.replace("%value%", alias);
			} else {
				result = map("1400B");
			}
			return result;

		} catch (SerigyException ex) {
			result = map("1400B");
		}
		return result;
	}

	private String parentKeyViolation(String message, String initialMessage) {
		String result = "";
		String regex = "\\(\\w+\\.\\w+\\)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(initialMessage);

		String owner = "";
		String name = "";
		if (matcher.find()) {
			String group = matcher.group();
			owner = group.substring(1, group.indexOf("."));
			name = group.substring(group.indexOf(".") + 1, group.indexOf(")"));
		}

		Constraint c = getDatabase().getConstraint(owner, name);
		if (c != null) {
			result = message.replace("%value%", c.getRef().getTable());
		} else {
			result = map("2291B");
		}
		return result;
	}

	private String childRecordViolation(String message, String initialMessage) {
		String result = "";
		String regex = "\\(\\w+\\.\\w+\\)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(initialMessage);

		String owner = "";
		String name = "";
		if (matcher.find()) {
			String group = matcher.group();
			owner = group.substring(1, group.indexOf("."));
			name = group.substring(group.indexOf(".") + 1, group.indexOf(")"));
		}

		Constraint c = getDatabase().getConstraint(owner, name);
		if (c != null) {
			String alias = "";
			try {
				AnnotationDescriptor descriptor = AnnotationDescriptorFinder.findDescriptor(c.getTable());
				if (descriptor != null) {
					alias = descriptor.getTableAlias();
					result = message.replace("%value%", alias);
				} else {
					result = map("2292B");
				}
			} catch (SerigyException ex) {
				result = map("2292B");
			}
		} else {
			result = map("2292B");
		}
		return result;
	}

	private String fieldValueTooBig(String message, String initialMessage) {
		String result = message;
		try {

			String column = "";
			String regex = "\"\\w+\"\\.\"\\w+\"\\.\"\\w+\"";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(initialMessage);
			if (matcher.find()) {
				String group = matcher.group();
				column = group.substring(group.lastIndexOf(".") + 2, group.lastIndexOf("\""));
			}

			AnnotationDescriptor descriptor = AnnotationDescriptorFinder.findDescriptor(column);

			String alias = "";
			if (descriptor != null) {
				alias = descriptor.getAliasFromColumnName(column);
			}

			if (!"".equals(alias)) {
				result = message.replace("%value%", alias);
			} else {
				return map("12899B");
			}
			int dot = initialMessage.lastIndexOf(":");
			int par = initialMessage.lastIndexOf(")");
			String maxLength = initialMessage.substring(dot + 2, par);
			result = result.replace("%max%", maxLength);

		} catch (SerigyException ex) {
			return map("12899B");
		}
		return result;
	}
	//</editor-fold>
}
