/*
 * Copyright 2010-2012 Niclas Hedhman.
 * Copyright 2011 Rickard Öberg.
 * Copyright 2013-2015 Paul Merlin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qi4j.library.conversion.values;

import java.util.Date;
import org.junit.Test;
import org.qi4j.api.constraint.ConstraintViolationException;
import org.qi4j.api.service.ServiceReference;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkCompletionException;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.functional.Function;
import org.qi4j.library.conversion.values.TestModel.PersonEntity;
import org.qi4j.library.conversion.values.TestModel.PersonValue;
import org.qi4j.library.conversion.values.TestModel.PersonValue2;
import org.qi4j.library.conversion.values.TestModel.PersonValue3;
import org.qi4j.library.conversion.values.TestModel.PersonValue4;
import org.qi4j.test.AbstractQi4jTest;
import org.qi4j.test.EntityTestAssembler;

import static org.junit.Assert.assertEquals;
import static org.qi4j.library.conversion.values.TestModel.createBirthDate;
import static org.qi4j.library.conversion.values.TestModel.createPerson;

public class EntityToValueTest
    extends AbstractQi4jTest
{
    @Override
    public void assemble( ModuleAssembly module )
        throws AssemblyException
    {
        // START SNIPPET: assembly
        new EntityToValueAssembler().assemble( module );
        // END SNIPPET: assembly
        new EntityTestAssembler().assemble( module );
        module.entities( PersonEntity.class );
        module.values( PersonValue.class );
        module.values( PersonValue2.class );
        module.values( PersonValue3.class );
        module.values( PersonValue4.class );
    }

    @Test
    public void whenConvertingEntityToValueExpectCorrectValues()
        throws UnitOfWorkCompletionException
    {
        UnitOfWork uow = module.newUnitOfWork();
        try
        {
            PersonEntity entity = setupPersonEntities( uow );

            // START SNIPPET: conversion
            EntityToValueService conversion = module.findService( EntityToValueService.class ).get();
            PersonValue value = conversion.convert( PersonValue.class, entity );
            // END SNIPPET: conversion
            assertEquals( "Niclas", value.firstName().get() );
            assertEquals( "Hedhman", value.lastName().get() );
            assertEquals( "id:Lis", value.spouse().get() );
            assertEquals( "id:Eric", value.children().get().get( 0 ) );
            uow.complete();
        }
        finally
        {
            uow.discard();
        }
    }

    @Test
    public void givenUnqualifiedValueWhenConvertingEntityExpectCorrectMapping()
        throws UnitOfWorkCompletionException
    {
        UnitOfWork uow = module.newUnitOfWork();
        try
        {
            PersonEntity niclas = setupPersonEntities( uow );

            ServiceReference<EntityToValueService> reference = module.findService( EntityToValueService.class );
            EntityToValueService service = reference.get();

            PersonValue2 niclasValue = service.convert( PersonValue2.class, niclas );
            assertEquals( "Niclas", niclasValue.firstName().get() );
            assertEquals( "Hedhman", niclasValue.lastName().get() );
            assertEquals( "id:Lis", niclasValue.spouse().get() );
            assertEquals( "id:Eric", niclasValue.children().get().get( 0 ) );
            uow.complete();
        }
        finally
        {
            uow.discard();
        }
    }

    @Test
    public void givenUnqualifiedValue2WhenConvertingEntityExpectCorrectMapping()
        throws UnitOfWorkCompletionException
    {
        UnitOfWork uow = module.newUnitOfWork();
        try
        {
            PersonEntity niclas = setupPersonEntities( uow );

            ServiceReference<EntityToValueService> reference = module.findService( EntityToValueService.class );
            EntityToValueService service = reference.get();

            PersonValue3 niclasValue = service.convert( PersonValue3.class, niclas );
            assertEquals( "Niclas", niclasValue.firstName().get() );
            assertEquals( "Hedhman", niclasValue.lastName().get() );
            assertEquals( "id:Lis", niclasValue.spouse().get() );
            assertEquals( "id:Eric", niclasValue.children().get().get( 0 ) );
            uow.complete();
        }
        finally
        {
            uow.discard();
        }
    }

    @Test( expected = ConstraintViolationException.class )
    public void givenQualifiedValueNotFromSameInterfaceWhenConvertingEntityExpectNonOptionalException()
        throws UnitOfWorkCompletionException
    {
        UnitOfWork uow = module.newUnitOfWork();
        try
        {
            PersonEntity niclas = setupPersonEntities( uow );

            ServiceReference<EntityToValueService> reference = module.findService( EntityToValueService.class );
            EntityToValueService service = reference.get();

            PersonValue4 niclasValue = service.convert( PersonValue4.class, niclas );
            uow.complete();
        }
        finally
        {
            uow.discard();
        }
    }

    @Test
    public void whenConvertingEntityToValueUsingPrototypeOpportunityExpectCorrectValues()
        throws UnitOfWorkCompletionException
    {
        UnitOfWork uow = module.newUnitOfWork();
        try
        {
            PersonEntity entity = setupPersonEntities( uow );

            // START SNIPPET: prototypeOpportunity
            EntityToValueService conversion = module.findService( EntityToValueService.class ).get();
            PersonValue value = conversion.convert( PersonValue.class, entity, new Function<PersonValue, PersonValue>()
            {
                @Override
                public PersonValue map( PersonValue prototype )
                {
                    prototype.firstName().set( "Prototype Opportunity" );
                    return prototype;
                }
            } );
            // END SNIPPET: prototypeOpportunity
            assertEquals( "Prototype Opportunity", value.firstName().get() );
            assertEquals( "Hedhman", value.lastName().get() );
            assertEquals( "id:Lis", value.spouse().get() );
            assertEquals( "id:Eric", value.children().get().get( 0 ) );
            uow.complete();
        }
        finally
        {
            uow.discard();
        }
    }

    private static PersonEntity setupPersonEntities( UnitOfWork uow )
    {
        PersonEntity niclas = createNiclas( uow );
        PersonEntity lis = createLis( uow );
        PersonEntity eric = createEric( uow );
        niclas.spouse().set( lis );
        niclas.children().add( eric );
        lis.spouse().set( niclas );
        lis.children().add( eric );
        assertEquals( "Niclas", niclas.firstName() );
        assertEquals( "Hedhman", niclas.lastName() );
        assertEquals( "Lis", lis.firstName() );
        assertEquals( "Gazi", lis.lastName() );
        assertEquals( "Eric", eric.firstName() );
        assertEquals( "Hedman", eric.lastName() );
        return niclas;
    }

    private static PersonEntity createNiclas( UnitOfWork uow )
    {
        String firstName = "Niclas";
        String lastName = "Hedhman";
        Date birthTime = createBirthDate( 1964, 9, 25 );
        return createPerson( uow, firstName, lastName, birthTime );
    }

    private static PersonEntity createLis( UnitOfWork uow )
    {
        String firstName = "Lis";
        String lastName = "Gazi";
        Date birthTime = createBirthDate( 1976, 2, 19 );
        return createPerson( uow, firstName, lastName, birthTime );
    }

    private static PersonEntity createEric( UnitOfWork uow )
    {
        String firstName = "Eric";
        String lastName = "Hedman";
        Date birthTime = createBirthDate( 2004, 4, 8 );
        return createPerson( uow, firstName, lastName, birthTime );
    }
}
