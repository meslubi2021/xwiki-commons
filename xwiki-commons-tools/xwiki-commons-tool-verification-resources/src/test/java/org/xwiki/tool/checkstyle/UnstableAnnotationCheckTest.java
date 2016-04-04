/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.tool.checkstyle;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.BaseCheckTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.utils.CommonUtils;

/**
 * Unit tests for {@link UnstableAnnotationCheck}.
 *
 * @version $Id$
 * @since 8.1M1
 */
public class UnstableAnnotationCheckTest extends BaseCheckTestSupport
{
    private DefaultConfiguration checkConfig;

    @Before
    public void setUp()
    {
        this.checkConfig = createCheckConfig(UnstableAnnotationCheck.class);
        this.checkConfig.addAttribute("currentVersion", "8.1-SNAPSHOT");
    }

    @Test
    public void checkWithNoSinceJavadocTag() throws Exception
    {
        final String[] expected = {
            "24:1: There is an @Unstable annotation for [org.xwiki.tool.checkstyle.test.TestClassWithNoSinceJavadocTag] "
                + "but the @since javadoc tag is missing, you must add it!"
        };

        verify(this.checkConfig, getPath("TestClassWithNoSinceJavadocTag.java"), expected);
    }

    @Test
    public void checkWithUnstableOkAtClassLevel() throws Exception
    {
        final String[] expected = CommonUtils.EMPTY_STRING_ARRAY;

        verify(this.checkConfig, getPath("TestClassWithUnstableOkAtClassLevel.java"), expected);
    }

    @Test
    public void checkWithUnstableAnnotationShouldBeRemoved() throws Exception
    {
        final String[] expected = {
            "29:1: The @Unstable annotation for [org.xwiki.tool.checkstyle.test."
            + "TestClassWithUnstableAnnotationShouldBeRemoved] must be removed since its been there for more than a "
            + "full development cycle (was introduced in [6.0] and current version is [8.1-SNAPSHOT])"
        };

        verify(this.checkConfig, getPath("TestClassWithUnstableAnnotationShouldBeRemoved.java"), expected);
    }

    @Override
    protected String getPath(String filename) throws IOException
    {
        return (new File("src/test/resources/org/xwiki/tool/checkstyle/test/" + filename)).getCanonicalPath();
    }
}
