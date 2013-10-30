/*
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
* Foundation.
*
* You should have received a copy of the GNU Lesser General Public License along with this
* program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
* or from the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU Lesser General Public License for more details.
*
* Copyright (c) 2001 - 2013 Object Refinery Ltd, Pentaho Corporation and Contributors..  All rights reserved.
*/

package org.pentaho.reporting.engine.classic.core.layout.process.layoutrules;

import org.pentaho.reporting.engine.classic.core.layout.model.RenderBox;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderNode;
import org.pentaho.reporting.engine.classic.core.layout.model.context.StaticBoxLayoutProperties;


/**
 * Anthing that is not text. This could be an image or an inline-block element. For now, we assume that these beasts are
 * not breakable at the end of the line (outer linebreaks).
 *
 * @author Thomas Morgner
 */
public class InlineBoxSequenceElement extends InlineNodeSequenceElement
{
  public static final InlineSequenceElement INSTANCE = new InlineBoxSequenceElement();

  private InlineBoxSequenceElement()
  {
  }

  /**
   * The width of the element. This is the minimum width of the element.
   *
   * @return
   */
  public long getMinimumWidth(final RenderNode node)
  {
    final RenderBox box = (RenderBox) node;
    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    return box.getMinimumChunkWidth() + blp.getMarginLeft() + blp.getMarginRight();
  }

  public long getMaximumWidth(final RenderNode node)
  {
    final RenderBox box = (RenderBox) node;
    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    return box.getMaximumBoxWidth() + blp.getMarginLeft() + blp.getMarginRight();
  }

  public boolean isPreserveWhitespace(final RenderNode node)
  {
    final RenderBox box = (RenderBox) node;
    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    return blp.isPreserveSpace();
  }
}
