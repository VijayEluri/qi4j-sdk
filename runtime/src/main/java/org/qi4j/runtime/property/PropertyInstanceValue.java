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

import org.qi4j.property.PropertyVetoException;
import org.qi4j.property.ReadableProperty;
import org.qi4j.property.WritableProperty;

/**
 * TODO
 */
public final class PropertyInstanceValue<T>
    implements ReadableProperty<T>, WritableProperty<T>
{
    PropertyInstance<T> instance;

    public void setInstance( PropertyInstance<T> instance )
    {
        this.instance = instance;
    }

    public T get()
    {
        return instance.read();
    }

    public void set( T newValue ) throws PropertyVetoException
    {
        instance.write( newValue );
    }
}