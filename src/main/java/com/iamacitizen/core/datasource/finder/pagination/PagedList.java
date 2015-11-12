package com.iamacitizen.core.datasource.finder.pagination;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class PagedList<T> extends ArrayList<T> {

    private PaginationContext context;

    public PagedList(int pageSize) {
        context = new PaginationContext(pageSize);
    }

    public PaginationContext getContext() {
        return context;
    }

    public void setContext(PaginationContext context) {
        this.context = context;
    }

    public boolean add(T object) {
        boolean result = super.add(object);
        //Atualiza o contexto da paginação
        return result;
    }
}
