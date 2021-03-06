/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.persistence.common.query;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opendaylight.persistence.DataStore;
import org.opendaylight.persistence.PersistenceException;
import org.opendaylight.persistence.Query;
import org.opendaylight.persistence.util.common.Instruction;
import org.opendaylight.persistence.util.common.Subroutine;

/**
 * Query that allows pre-processing and post-processing from the business logic.
 * <p>
 * This query decouples business logic from persistence logic when the result of the business logic
 * execution affects whether the query is committed or not. For example, if after persisting a model
 * object some business logic is executed, and the failure of such execution requires the database
 * updates to be rolled back
 * <P>
 * Using this decorator the pre-processing and post-processing logic is executed in the context of
 * the query, which in most cases will be executed in a database transaction if transactions are
 * supported.
 *
 * @param <T> type of the query result
 * @param <C> type of the query's execution context; the context managed by the {@link DataStore}
 * @author Fabiel Zuniga
 * @author Nachiket Abhyankar
 */
public final class QueryModelDecorator<T, C> implements Query<T, C> {

    private Instruction preProcessing;
    private Subroutine<T> postProcessing;
    private Query<T, C> delegate;

    /**
     * Creates a write query decorator.
     *
     * @param delegate query delegate
     * @param preProcessing pre-processing
     * @param postProcessing post-processing
     */
    public QueryModelDecorator(@Nonnull Query<T, C> delegate, @Nullable Instruction preProcessing,
            @Nullable Subroutine<T> postProcessing) {
        this.delegate = delegate;
        this.preProcessing = preProcessing;
        this.postProcessing = postProcessing;
    }

    @Override
    public T execute(C context) throws PersistenceException {

        if (this.preProcessing != null) {
            this.preProcessing.execute();
        }

        T result = this.delegate.execute(context);

        if (this.postProcessing != null) {
            this.postProcessing.execute(result);
        }

        return result;
    }
}
