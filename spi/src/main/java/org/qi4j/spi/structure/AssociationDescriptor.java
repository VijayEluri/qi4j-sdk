/*
 * Copyright (c) 2007, Rickard �berg. All Rights Reserved.
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

package org.qi4j.spi.structure;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * TODO
 */
public final class AssociationDescriptor
{
    private Class associationType;
    private Class associatedType;
    private Map<Class, Object> associationInfos;
    private Method accessor;

    public AssociationDescriptor( Class associationType, Class associatedType, Map<Class, Object> associationInfos, Method accessor )
    {
        this.associationType = associationType;
        this.associatedType = associatedType;
        this.associationInfos = associationInfos;
        this.accessor = accessor;
    }

    public Class getAssociationType()
    {
        return associationType;
    }

    public Class getAssociatedType()
    {
        return associatedType;
    }

    public Map<Class, Object> getAssociationInfos()
    {
        return associationInfos;
    }

    public Method getAccessor()
    {
        return accessor;
    }
}
