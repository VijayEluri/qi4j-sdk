/*
 * Copyright (c) 2012, Kent Sølvsten. All Rights Reserved.
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
package org.qi4j.runtime.value;

import org.qi4j.api.association.AssociationStateHolder;
import org.qi4j.api.common.ConstructionException;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.runtime.composite.StateResolver;
import org.qi4j.runtime.structure.ModelModule;
import org.qi4j.runtime.structure.ModuleInstance;

public class ValueBuilderWithState<T> implements ValueBuilder<T>
{
    private final ModelModule<ValueModel> model;
    private ValueInstance prototypeInstance;

    public ValueBuilderWithState( ModelModule<ValueModel> compositeModelModule,
                                  ModuleInstance currentModule,
                                  StateResolver stateResolver )
    {
        ValueStateInstance state = new ValueStateInstance( compositeModelModule, currentModule, stateResolver );
        ValueInstance instance = compositeModelModule.model().newValueInstance( compositeModelModule.module(), state );
        instance.prepareToBuild();
        this.model = compositeModelModule;
        this.prototypeInstance = instance;
    }

    @Override
    public T prototype()
    {
        verifyUnderConstruction();
        return prototypeInstance.<T>proxy();
    }

    @Override
    public AssociationStateHolder state()
    {
        verifyUnderConstruction();
        return prototypeInstance.state();
    }

    @Override
    public <K> K prototypeFor( Class<K> mixinType )
    {
        verifyUnderConstruction();

        return prototypeInstance.newProxy( mixinType );
    }

    @Override
    public T newInstance()
        throws ConstructionException
    {
        verifyUnderConstruction();

        // Set correct info's (immutable) on the state
        prototypeInstance.prepareBuilderState();

        // Check that it is valid
        model.model().checkConstraints( prototypeInstance.state() );

        try
        {
            return prototypeInstance.<T>proxy();
        }
        finally
        {
            // Invalidate builder
            prototypeInstance = null;
        }
    }

    private void verifyUnderConstruction()
    {
        if( prototypeInstance == null )
        {
            throw new IllegalStateException( "ValueBuilder instances cannot be reused" );
        }
    }
}
