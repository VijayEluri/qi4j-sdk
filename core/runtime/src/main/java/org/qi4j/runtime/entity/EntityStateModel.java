/*
 * Copyright (c) 2008-2011, Rickard Öberg. All Rights Reserved.
 * Copyright (c) 2008-2013, Niclas Hedhman. All Rights Reserved.
 * Copyright (c) 2014, Paul Merlin. All Rights Reserved.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.qi4j.runtime.entity;

import java.lang.reflect.AccessibleObject;
import org.qi4j.api.association.AssociationDescriptor;
import org.qi4j.api.association.AssociationStateDescriptor;
import org.qi4j.api.common.QualifiedName;
import org.qi4j.functional.HierarchicalVisitor;
import org.qi4j.functional.VisitableHierarchy;
import org.qi4j.runtime.association.AssociationModel;
import org.qi4j.runtime.association.AssociationsModel;
import org.qi4j.runtime.association.ManyAssociationModel;
import org.qi4j.runtime.association.ManyAssociationsModel;
import org.qi4j.runtime.association.NamedAssociationModel;
import org.qi4j.runtime.association.NamedAssociationsModel;
import org.qi4j.runtime.composite.StateModel;
import org.qi4j.runtime.property.PropertiesModel;

/**
 * Model for EntityComposite state.
 */
public final class EntityStateModel
    extends StateModel
    implements AssociationStateDescriptor
{
    private final AssociationsModel associationsModel;
    private final ManyAssociationsModel manyAssociationsModel;
    private final NamedAssociationsModel namedAssociationsModel;

    public EntityStateModel( PropertiesModel propertiesModel,
                             AssociationsModel associationsModel,
                             ManyAssociationsModel manyAssociationsModel,
                             NamedAssociationsModel namedAssociationsModel )
    {
        super( propertiesModel );
        this.associationsModel = associationsModel;
        this.manyAssociationsModel = manyAssociationsModel;
        this.namedAssociationsModel = namedAssociationsModel;
    }

    public AssociationModel getAssociation( AccessibleObject accessor )
        throws IllegalArgumentException
    {
        return associationsModel.getAssociation( accessor );
    }

    @Override
    public AssociationDescriptor getAssociationByName( String name )
        throws IllegalArgumentException
    {
        return associationsModel.getAssociationByName( name );
    }

    @Override
    public AssociationDescriptor getAssociationByQualifiedName( QualifiedName name )
        throws IllegalArgumentException
    {
        return associationsModel.getAssociationByQualifiedName( name );
    }

    public ManyAssociationModel getManyAssociation( AccessibleObject accessor )
        throws IllegalArgumentException
    {
        return manyAssociationsModel.getManyAssociation( accessor );
    }

    @Override
    public AssociationDescriptor getManyAssociationByName( String name )
        throws IllegalArgumentException
    {
        return manyAssociationsModel.getManyAssociationByName( name );
    }

    @Override
    public AssociationDescriptor getManyAssociationByQualifiedName( QualifiedName name )
        throws IllegalArgumentException
    {
        return manyAssociationsModel.getManyAssociationByQualifiedName( name );
    }

    public NamedAssociationModel getNamedAssociation( AccessibleObject accessor )
        throws IllegalArgumentException
    {
        return namedAssociationsModel.getNamedAssociation( accessor );
    }

    @Override
    public AssociationDescriptor getNamedAssociationByName( String name )
        throws IllegalArgumentException
    {
        return namedAssociationsModel.getNamedAssociationByName( name );
    }

    @Override
    public AssociationDescriptor getNamedAssociationByQualifiedName( QualifiedName name )
        throws IllegalArgumentException
    {
        return namedAssociationsModel.getNamedAssociationByQualifiedName( name );
    }

    @Override
    public Iterable<AssociationModel> associations()
    {
        return associationsModel.associations();
    }

    @Override
    public Iterable<ManyAssociationModel> manyAssociations()
    {
        return manyAssociationsModel.manyAssociations();
    }

    @Override
    public Iterable<NamedAssociationModel> namedAssociations()
    {
        return namedAssociationsModel.namedAssociations();
    }

    @Override
    public <ThrowableType extends Throwable> boolean accept( HierarchicalVisitor<? super Object, ? super Object, ThrowableType> visitor )
        throws ThrowableType
    {
        if( visitor.visitEnter( this ) )
        {
            if( ( (VisitableHierarchy<Object, Object>) propertiesModel ).accept( visitor ) )
            {
                if( ( (VisitableHierarchy<AssociationsModel, AssociationModel>) associationsModel ).accept( visitor ) )
                {
                    if( ( (VisitableHierarchy<ManyAssociationsModel, ManyAssociationModel>) manyAssociationsModel ).accept( visitor ) )
                    {
                        ( (VisitableHierarchy<NamedAssociationsModel, NamedAssociationModel>) namedAssociationsModel ).accept( visitor );
                    }
                }
            }
        }
        return visitor.visitLeave( this );
    }

}
