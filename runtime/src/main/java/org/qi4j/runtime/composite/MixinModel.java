/*
 * Copyright (c) 2008, Rickard Öberg. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.qi4j.runtime.composite;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.qi4j.composite.Composite;
import org.qi4j.composite.State;
import org.qi4j.injection.scope.This;
import org.qi4j.runtime.injection.DependencyModel;
import org.qi4j.runtime.injection.InjectedFieldsModel;
import org.qi4j.runtime.injection.InjectedMethodsModel;
import org.qi4j.runtime.injection.InjectionContext;
import org.qi4j.runtime.structure.Binder;
import org.qi4j.runtime.structure.DependencyVisitor;
import org.qi4j.runtime.structure.ModelVisitor;
import org.qi4j.spi.composite.CompositeInstance;

/**
 * TODO
 */
public final class MixinModel
    implements Binder
{
    // Model
    private Class mixinClass;
    private ConstructorsModel constructorsModel;
    private InjectedFieldsModel injectedFieldsModel;
    private InjectedMethodsModel injectedMethodsModel;
    private ConcernsDeclaration concernsDeclaration;
    private SideEffectsDeclaration sideEffectsDeclaration;
    private Set<Class> thisMixinTypes = Collections.EMPTY_SET;

    public MixinModel( Class mixinClass )
    {
        this.mixinClass = mixinClass;

        constructorsModel = new ConstructorsModel( mixinClass );
        injectedFieldsModel = new InjectedFieldsModel( mixinClass );
        injectedMethodsModel = new InjectedMethodsModel( mixinClass );

        concernsDeclaration = new ConcernsDeclaration( mixinClass );
        sideEffectsDeclaration = new SideEffectsDeclaration( mixinClass );

        visitModel( new DependencyVisitor( new DependencyModel.ScopeSpecification( This.class ) )
        {
            public void visitDependency( DependencyModel dependencyModel )
            {
                if( thisMixinTypes == Collections.EMPTY_SET )
                {
                    thisMixinTypes = new HashSet<Class>();
                }
                thisMixinTypes.add( dependencyModel.rawInjectionType() );
            }
        } );
    }

    public Class mixinClass()
    {
        return mixinClass;
    }

    public void visitModel( ModelVisitor modelVisitor )
    {
        modelVisitor.visit( this );

        constructorsModel.visitModel( modelVisitor );
        injectedFieldsModel.visitModel( modelVisitor );
        injectedMethodsModel.visitModel( modelVisitor );
    }

    // Binding
    public void bind( Resolution context ) throws BindingException
    {
        constructorsModel.bind( context );
        injectedFieldsModel.bind( context );
        injectedMethodsModel.bind( context );
    }

    // Context
    public Object newInstance( CompositeInstance compositeInstance, UsesInstance uses, State state )
    {
        InjectionContext injectionContext = new InjectionContext( compositeInstance, uses, state );
        Object mixin = constructorsModel.newInstance( injectionContext );
        injectedFieldsModel.inject( injectionContext, mixin );
        injectedMethodsModel.inject( injectionContext, mixin );
        return mixin;
    }

    public Set<Class> thisMixinTypes()
    {
        return thisMixinTypes;
    }

    protected FragmentInvocationHandler newInvocationHandler( Class methodClass )
    {
        if( InvocationHandler.class.isAssignableFrom( mixinClass ) && !methodClass.isAssignableFrom( mixinClass ) )
        {
            return new GenericFragmentInvocationHandler();
        }
        else
        {
            return new TypedFragmentInvocationHandler();
        }

    }

    public MethodConcernsModel concernsFor( Method method, Class<? extends Composite> type )
    {
        return concernsDeclaration.concernsFor( method, type );
    }


    public MethodSideEffectsModel sideEffectsFor( Method method, Class<? extends Composite> type )
    {
        return sideEffectsDeclaration.sideEffectsFor( method, type );
    }

    @Override public String toString()
    {
        return mixinClass.getName();
    }
}