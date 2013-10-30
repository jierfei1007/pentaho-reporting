/*!
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
* Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
*/

package org.pentaho.reporting.engine.classic.extensions.parsers.reportdesigner.datasets;

import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.NamedStaticDataFactory;
import org.pentaho.reporting.libraries.base.util.LinkedMap;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.IgnoreAnyChildReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class StaticDataSetReadHandler extends AbstractXmlReadHandler
{
  private String queryName;
  private String className;
  private String methodName;
  private StaticDataSetParametersReadHandler parameters;
  private NamedStaticDataFactory staticDataFactory;

  public StaticDataSetReadHandler()
  {
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws SAXException if there is a parsing error.
   */
  protected void startParsing(final Attributes attrs) throws SAXException
  {
    queryName = attrs.getValue(getUri(), "queryName");
    if (queryName == null)
    {
      throw new ParseException("Attribute 'queryName' must be given");
    }

    className = attrs.getValue(getUri(), "className");
    if (className == null)
    {
      throw new ParseException("Attribute 'className' must be given");
    }

    methodName = attrs.getValue(getUri(), "methodName");
    if (methodName == null)
    {
      throw new ParseException("Attribute 'methodName' must be given");
    }
  }

  /**
   * Returns the handler for a child element.
   *
   * @param uri     the URI of the namespace of the current element.
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri, final String tagName, final Attributes atts) throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }
    if ("padding".equals(tagName))
    {
      return new IgnoreAnyChildReadHandler();
    }

    if ("parameters".equals(tagName))
    {
      parameters = new StaticDataSetParametersReadHandler();
      return parameters;
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException
  {
    final LinkedMap map = (LinkedMap) parameters.getObject();
    staticDataFactory = new NamedStaticDataFactory();
    if (map.isEmpty())
    {
      staticDataFactory.setQuery(queryName, className + '#' + methodName);
    }
    else
    {
      String query = className + '#' + methodName + '(';
      final Object[] objects = map.keys();
      for (int i = 0; i < objects.length; i++)
      {
        if (i != 0)
        {
          query += ",";
        }
        query += String.valueOf(objects[i]);
      }
      staticDataFactory.setQuery(queryName, query);
    }
  }

  /**
   * Returns the object for this element or null, if this element does
   * not create an object.
   *
   * @return the object.
   * @throws SAXException if an parser error occured.
   */
  public Object getObject() throws SAXException
  {
    return staticDataFactory;
  }
}
