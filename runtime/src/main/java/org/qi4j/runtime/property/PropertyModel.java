/*
 * Copyright (c) 2007, Rickard Öberg. All Rights Reserved.
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

package org.qi4j.runtime.property;

import org.qi4j.api.common.MetaInfo;
import org.qi4j.api.property.Property;
import org.qi4j.runtime.composite.ValueConstraintsInstance;

import java.lang.reflect.AccessibleObject;

/**
 * Implementation of Properties for Transient Composites
 */
public class PropertyModel
    extends AbstractPropertyModel
{
    public PropertyModel( AccessibleObject anAccessor, boolean immutable, ValueConstraintsInstance constraints,
                          MetaInfo metaInfo, Object anInitialValue
    )
    {
        super( anAccessor, immutable, constraints, metaInfo, anInitialValue );
    }

    @SuppressWarnings( "unchecked" )
    public <T> Property<T> newInstance( Object value )
    {
        Property property;
        property = new PropertyInstance<Object>( this, value, this );
        return wrapProperty( property );
    }
}
