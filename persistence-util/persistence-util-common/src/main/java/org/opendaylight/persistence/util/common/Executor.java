/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.persistence.util.common;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

/**
 * Sequence of instructions that perform a specific task, packaged as a unit.
 * <p>
 * The terms {@link Instruction}, {@link Procedure}, {@link Subroutine} and {@link Executor} (or
 * function) are really synonyms. There were used just to denote the difference in the return type
 * and input parameters. Note: {@link Runnable} and {@link Callable} were not reused because they
 * have a special meaning that is usually related to asynchronous executions. The following
 * summarizes differences adopted here:
 *
 * <pre>
 * public void execute(); // Instruction
 *
 * public T execute(); // Procedure
 *
 * public void execute(I input); // Subroutine
 *
 * public T execute(I input); // Executor
 * </pre>
 *
 * A checked exception is not thrown by the execute method because unexpected conditions should be
 * handled by the callable unit.
 *
 * @param <T> type of the execution result
 * @param <I> type of the execution input
 * @see Instruction
 * @see Procedure
 * @see Subroutine
 * @author Fabiel Zuniga
 * @author Nachiket Abhyankar
 */
public interface Executor<T, I> {

    /**
     * Executes the instructions.
     *
     * @param input input
     * @return execution result
     */
    T execute(@Nullable I input);
}
