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

package org.qi4j.api.configuration;

import org.qi4j.api.entity.EntityComposite;
import org.qi4j.api.entity.Queryable;

/**
 * Services that want to be configurable should have a ConfigurationComposite that contains all the settings.
 * They are treated as EntityComposites, and are therefore stored in an EntityStore. There will be one instance
 * per service instance that uses each ConfigurationComposite, and the identity of the entity is the same as that
 * of the service.
 */
@Queryable( false )
public interface ConfigurationComposite
    extends EntityComposite
{
}
