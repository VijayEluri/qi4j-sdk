/*
 * Copyright 2007 Edward Yakop.
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
package org.ops4j.orthogon.samples.common.mixin;

import java.net.URL;
import java.util.Collections;
import java.util.List;

public final class PictureMixin implements Picture
{
    private List<URL> m_pictures;

    public PictureMixin()
    {
    }

    public List<URL> getPictures()
    {
        return m_pictures;
    }

    public void setPictures( List<URL> pictures )
    {
        if( pictures == null )
        {
            m_pictures = Collections.emptyList();
        }
        else
        {
            m_pictures = pictures;
        }
    }
}
