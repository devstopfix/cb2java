/* This file was generated by SableCC (http://www.sablecc.org/). */
package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

import java.util.*;


public final class ATrailingLeadingOrTrailing extends PLeadingOrTrailing
{
    private TTrailing _trailing_;

    public ATrailingLeadingOrTrailing ()
    {
    }

    public ATrailingLeadingOrTrailing (TTrailing _trailing_)
    {
        setTrailing (_trailing_);
    }

    public Object clone ()
    {
        return new ATrailingLeadingOrTrailing((TTrailing) cloneNode (_trailing_));
    }

    public void apply (Switch sw)
    {
        ((Analysis) sw).caseATrailingLeadingOrTrailing (this);
    }

    public TTrailing getTrailing ()
    {
        return _trailing_;
    }

    public void setTrailing (TTrailing node)
    {
        if (_trailing_ != null)
        {
            _trailing_.parent (null);
        }

        if (node != null)
        {
            if (node.parent () != null)
            {
                node.parent ().removeChild (node);
            }

            node.parent (this);
        }

        _trailing_ = node;
    }

    public String toString ()
    {
        return "" + toString (_trailing_);
    }

    void removeChild (Node child)
    {
        if (_trailing_ == child)
        {
            _trailing_ = null;

            return;
        }
    }

    void replaceChild (Node oldChild, Node newChild)
    {
        if (_trailing_ == oldChild)
        {
            setTrailing ((TTrailing) newChild);

            return;
        }
    }
}
