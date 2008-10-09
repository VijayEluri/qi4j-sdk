package org.qi4j.runtime.injection.provider;

import org.qi4j.composite.CompositeBuilderFactory;
import org.qi4j.composite.ConstructionException;
import org.qi4j.composite.NoSuchCompositeException;
import org.qi4j.object.NoSuchObjectException;
import org.qi4j.object.ObjectBuilderFactory;
import org.qi4j.runtime.composite.Resolution;
import org.qi4j.runtime.composite.UsesInstance;
import org.qi4j.runtime.injection.DependencyModel;
import org.qi4j.runtime.injection.InjectionContext;
import org.qi4j.runtime.injection.InjectionProvider;
import org.qi4j.runtime.injection.InjectionProviderFactory;
import org.qi4j.runtime.structure.ModuleInstance;

/**
 * TODO
 */
public final class UsesInjectionProviderFactory
    implements InjectionProviderFactory
{
    public UsesInjectionProviderFactory()
    {
    }

    public InjectionProvider newInjectionProvider( Resolution resolution, DependencyModel dependencyModel ) throws InvalidInjectionException
    {
        return new UsesInjectionProvider( dependencyModel );
    }

    private class UsesInjectionProvider implements InjectionProvider
    {
        private final DependencyModel dependency;

        public UsesInjectionProvider( DependencyModel dependency )
        {
            this.dependency = dependency;
        }

        @SuppressWarnings( "unchecked" )
        public Object provideInjection( InjectionContext context )
            throws InjectionProviderException
        {
            UsesInstance uses = context.uses();

            Class injectionType = dependency.rawInjectionType();
            Object usesObject = uses.useForType( injectionType );

            if( usesObject == null )
            {
                // No @Uses object provided
                // Try instantiating a Composite or Object for the given type
                ModuleInstance moduleInstance = context.moduleInstance();
                try
                {
                    CompositeBuilderFactory compositeBF = moduleInstance.compositeBuilderFactory();
                    return compositeBF.newComposite( injectionType );
                }
                catch( NoSuchCompositeException e )
                {
                    // Retry for object
                    return createObject( injectionType, moduleInstance );
                }
                catch( ConstructionException e )
                {
                    // Retry for object
                    return createObject( injectionType, moduleInstance );
                }
            }
            else
            {
                return usesObject;
            }
        }

        @SuppressWarnings( "unchecked" )
        private Object createObject( Class injectionType, ModuleInstance moduleInstance )
        {
            try
            {
                ObjectBuilderFactory objectBuilderFactory = moduleInstance.objectBuilderFactory();
                return objectBuilderFactory.newObject( injectionType );
            }
            catch( NoSuchObjectException e1 )
            {
                return null;
            }
            catch( ConstructionException e2 )
            {
                return null;
            }
        }
    }
}