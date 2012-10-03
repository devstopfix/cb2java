/* This file was generated by SableCC (http://www.sablecc.org/). */
package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

import java.util.*;


public final class AGlobalClauseClause extends PClause
{
    private PGlobalClause _globalClause_;

    public AGlobalClauseClause ()
    {
    }

    public AGlobalClauseClause (PGlobalClause _globalClause_)
    {
        setGlobalClause (_globalClause_);
    }

    public Object clone ()
    {
        return new AGlobalClauseClause((PGlobalClause) cloneNode (
                _globalClause_));
    }

    public void apply (Switch sw)
    {
        ((Analysis) sw).caseAGlobalClauseClause (this);
    }

    public PGlobalClause getGlobalClause ()
    {
        return _globalClause_;
    }

    public void setGlobalClause (PGlobalClause node)
    {
        if (_globalClause_ != null)
        {
            _globalClause_.parent (null);
        }

        if (node != null)
        {
            if (node.parent () != null)
            {
                node.parent ().removeChild (node);
            }

            node.parent (this);
        }

        _globalClause_ = node;
    }

    public String toString ()
    {
        return "" + toString (_globalClause_);
    }

    void removeChild (Node child)
    {
        if (_globalClause_ == child)
        {
            _globalClause_ = null;

            return;
        }
    }

    void replaceChild (Node oldChild, Node newChild)
    {
        if (_globalClause_ == oldChild)
        {
            setGlobalClause ((PGlobalClause) newChild);

            return;
        }
    }
}
