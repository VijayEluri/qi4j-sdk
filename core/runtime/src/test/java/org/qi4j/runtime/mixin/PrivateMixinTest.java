/*
 * Copyright (c) 2008, Rickard Öberg. All Rights Reserved.
 * Copyright (c) 2008, Niclas Hedhman. All Rights Reserved.
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

package org.qi4j.runtime.mixin;

import org.junit.Test;
import org.qi4j.api.composite.TransientComposite;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Unit tests related to private mixins.
 */
public class PrivateMixinTest
    extends AbstractQi4jTest
{
    public void assemble( ModuleAssembly module )
        throws AssemblyException
    {
        module.transients( SpeakComposite.class );
    }

    /**
     * Tests that private mixins are injected correctly.
     */
    @Test
    public void privateMixinInjection()
    {
        SpeakComposite test = module.newTransient( SpeakComposite.class );
        assertThat( "Speak", test.speak(), is( equalTo( "I say it works" ) ) );
    }

    @Mixins( SpeakMixin.class )
    public interface Speak
    {
        String speak();
    }

    public static class SpeakMixin
        implements Speak
    {
        @This
        Word word;

        public String speak()
        {
            return "I say " + word.get();
        }
    }

    @Mixins( { WordMixin.class } )
    public interface SpeakComposite
        extends Speak, TransientComposite
    {
    }

    public interface Word
    {
        String get();
    }

    public static class WordMixin
        implements Word
    {
        public String get()
        {
            return "it works";
        }
    }
}
