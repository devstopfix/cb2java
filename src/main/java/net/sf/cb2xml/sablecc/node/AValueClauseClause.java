/* This file was generated by SableCC (http://www.sablecc.org/). */
package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

import java.util.*;


public final class AValueClauseClause extends PClause
{
    private PValueClause _valueClause_;

    public AValueClauseClause ()
    {
    }

    public AValueClauseClause (PValueClause _valueClause_)
    {
        setValueClause (_valueClause_);
    }

    public Object clone ()
    {
        return new AValueClauseClause((PValueClause) cloneNode (_valueClause_));
    }

    public void apply (Switch sw)
    {
        ((Analysis) sw).caseAValueClauseClause (this);
    }

    public PValueClause getValueClause ()
    {
        return _valueClause_;
    }

    public void setValueClause (PValueClause node)
    {
        if (_valueClause_ != null)
        {
            _valueClause_.parent (null);
        }

        if (node != null)
        {
            if (node.parent () != null)
            {
                node.parent ().removeChild (node);
            }

            node.parent (this);
        }

        _valueClause_ = node;
    }

    public String toString ()
    {
        return "" + toString (_valueClause_);
    }

    void removeChild (Node child)
    {
        if (_valueClause_ == child)
        {
            _valueClause_ = null;

            return;
        }
    }

    void replaceChild (Node oldChild, Node newChild)
    {
        if (_valueClause_ == oldChild)
        {
            setValueClause ((PValueClause) newChild);

            return;
        }
    }
}
