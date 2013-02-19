package org.pentaho.reporting.library.parameter.defaults;

import java.io.IOException;

import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.xmlns.common.AttributeList;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriter;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriterSupport;
import org.pentaho.reporting.library.parameter.LibParameterBoot;
import org.pentaho.reporting.library.parameter.ListParameter;
import org.pentaho.reporting.library.parameter.ParameterContext;
import org.pentaho.reporting.library.parameter.ParameterDataTable;
import org.pentaho.reporting.library.parameter.ParameterException;
import org.pentaho.reporting.library.parameter.values.ConverterRegistry;
import org.pentaho.reporting.library.parameter.values.ValueConversionException;

public class DefaultListParameter extends AbstractParameter implements ListParameter
{
  private boolean strictValueCheck;
  private boolean allowMultiSelection;
  private boolean allowResetOnInvalidValue;
  private String keyColumn;
  private String textColumn;
  private String query;

  public DefaultListParameter(final String name, final Class valueType, final String query, final String keyColumn)
  {
    super(name, valueType);
    if (keyColumn == null)
    {
      throw new NullPointerException();
    }
    this.query = query;
    this.keyColumn = keyColumn;
    this.textColumn = keyColumn;
  }

  public boolean isStrictValueCheck()
  {
    return strictValueCheck;
  }

  public void setStrictValueCheck(final boolean strictValueCheck)
  {
    this.strictValueCheck = strictValueCheck;
  }

  public boolean isAllowMultiSelection()
  {
    return allowMultiSelection;
  }

  public void setAllowMultiSelection(final boolean allowMultiSelection)
  {
    this.allowMultiSelection = allowMultiSelection;
  }

  public boolean isAllowResetOnInvalidValue()
  {
    return allowResetOnInvalidValue;
  }

  public void setAllowResetOnInvalidValue(final boolean allowResetOnInvalidValue)
  {
    this.allowResetOnInvalidValue = allowResetOnInvalidValue;
  }

  public ParameterDataTable getValues(final ParameterContext context) throws ParameterException
  {
    return context.getDataFactory().performQuery(query, context.getParameterData());
  }

  public String getKeyColumn()
  {
    return keyColumn;
  }

  public void setKeyColumn(final String keyColumn)
  {
    if (keyColumn == null)
    {
      throw new NullPointerException();
    }
    this.keyColumn = keyColumn;
  }

  public String getTextColumn()
  {
    return textColumn;
  }

  public void setTextColumn(final String textColumn)
  {
    if (textColumn == null)
    {
      throw new NullPointerException();
    }
    this.textColumn = textColumn;
  }

  public String getQuery()
  {
    return query;
  }

  public void setQuery(final String query)
  {
    this.query = query;
  }

  public void toXml(final XmlWriter writer) throws IOException
  {
    if (StringUtils.isEmpty(getName()))
    {
      throw new IOException("Cannot write a unnamed parameter entry.");
    }

    final AttributeList paramAttrs = new AttributeList();
    paramAttrs.setAttribute(LibParameterBoot.NAMESPACE, "name", getName());
    paramAttrs.setAttribute(LibParameterBoot.NAMESPACE, "allow-multi-selection", // NON-NLS
        String.valueOf(isAllowMultiSelection()));
    paramAttrs.setAttribute(LibParameterBoot.NAMESPACE, "strict-values", // NON-NLS
        String.valueOf(isStrictValueCheck()));
    paramAttrs.setAttribute(LibParameterBoot.NAMESPACE, "mandatory", String.valueOf(isMandatory()));// NON-NLS
    paramAttrs.setAttribute(LibParameterBoot.NAMESPACE, "type", getValueType().getName());// NON-NLS
    if (StringUtils.isEmpty(getQuery()) == false)
    {
      paramAttrs.setAttribute(LibParameterBoot.NAMESPACE, "query", getQuery());// NON-NLS
    }
    if (StringUtils.isEmpty(getKeyColumn()) == false)
    {
      paramAttrs.setAttribute(LibParameterBoot.NAMESPACE, "key-column", getKeyColumn());// NON-NLS
    }
    if (StringUtils.isEmpty(getTextColumn()) == false)
    {
      paramAttrs.setAttribute(LibParameterBoot.NAMESPACE, "value-column", getTextColumn());// NON-NLS
    }

    final Object defaultValue = getDefaultValue();
    if (defaultValue != null)
    {
      try
      {
        final String valAsString = ConverterRegistry.toAttributeValue(defaultValue);
        if (StringUtils.isEmpty(valAsString) == false)
        {
          paramAttrs.setAttribute(LibParameterBoot.NAMESPACE, "default-value", valAsString);// NON-NLS
        }
      }
      catch (ValueConversionException e)
      {
        throw new IOException("Unable to convert parameter " +
            "default-value to string for parameter '" + getName() + '\'');
      }
    }
    final String[] namespaces = getParameterAttributeNamespaces();
    if (namespaces.length == 0)
    {
      writer.writeTag(LibParameterBoot.NAMESPACE, "list-parameter", paramAttrs, XmlWriterSupport.CLOSE);// NON-NLS
    }
    else
    {
      writer.writeTag(LibParameterBoot.NAMESPACE, "list-parameter", paramAttrs, XmlWriterSupport.OPEN);// NON-NLS
      for (int j = 0; j < namespaces.length; j++)
      {
        final String namespace = namespaces[j];
        final String[] names = getParameterAttributeNames(namespace);
        for (int k = 0; k < names.length; k++)
        {
          final String name = names[k];
          final String value = getParameterAttribute(namespace, name);
          if (StringUtils.isEmpty(value))
          {
            continue;
          }

          final AttributeList attrsAttr = new AttributeList();
          attrsAttr.setAttribute(LibParameterBoot.NAMESPACE, "namespace", namespace); // NON-NLS
          attrsAttr.setAttribute(LibParameterBoot.NAMESPACE, "name", name); // NON-NLS
          writer.writeTag(LibParameterBoot.NAMESPACE, "attribute", attrsAttr, XmlWriterSupport.OPEN);// NON-NLS
          writer.writeTextNormalized(value, false);
          writer.writeCloseTag();
        }
      }
      writer.writeCloseTag();
    }
  }
}
