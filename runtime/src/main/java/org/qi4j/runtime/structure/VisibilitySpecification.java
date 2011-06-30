package org.qi4j.runtime.structure;

import org.qi4j.api.common.Visibility;
import org.qi4j.api.object.ObjectDescriptor;
import org.qi4j.functional.Specification;

/**
 * TODO
 */
public class VisibilitySpecification
    implements Specification<ObjectDescriptor>
{
    public static final Specification<ObjectDescriptor> MODULE = new VisibilitySpecification( Visibility.module );
    public static final Specification<ObjectDescriptor> LAYER = new VisibilitySpecification( Visibility.layer );
    public static final Specification<ObjectDescriptor> APPLICATION = new VisibilitySpecification( Visibility.application );

    private Visibility visibility;

    public VisibilitySpecification(Visibility visibility)
    {
        this.visibility = visibility;
    }

    @Override
    public boolean satisfiedBy( ObjectDescriptor item )
    {
        return item.visibility().ordinal() >= visibility.ordinal();
    }
}
