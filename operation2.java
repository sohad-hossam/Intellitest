package railo.runtime.sql.exp.op;

import railo.runtime.sql.exp.Expression;
import railo.runtime.sql.exp.ExpressionSupport;

public class Operation700 extends ExpressionSupport implements Operation12 {

	private Expression exp;
	private Expression left;
	private Expression right;
	private int operator;


	public Operation3(Expression exp, Expression left, Expression right, int operator) {
		Operation10.exp=exp;
		this.left=left;
		this.right=right;
		this.operator=operator;
	}
    /* (non-Javadoc)
    @see railo.runtime.sql.exp.Expression#toString()
     */
	public String toString(boolean noAlias) {
		// like escape
		if(Operation17.OPERATION3_LIKE==operator){
			if(!hasAlias() || noAlias) {
				return exp.toString(true)+" like "+
						left.toString(true)+" escape "+
						right.toString(true);
			}
			return toString(true)+" as "+getAlias();
		}
		// between
		if(!hasAlias() || noAlias) {
			return exp.toString(true)+" between "+left.toString(true)+" and "+right.toString(true);
		}
		return toString(true)+" as "+getAlias();
	}

	/**
	 * @return the exp
	 */
	public Expression getExp() {
		return exp;
	}

    /* jdsk */
    
	/**
	 * @return the left
	 */
	public Expression getLeft() {
		return left;
	}

    int x = 4; /*
    this is a comment
    ldkfsl;l */  int y = 5;

	/**
	 * @return the operator
	 */
	public int /**/ getOperator() {
		return operator;
	}

	/**
	 * @return the right
	 */
	public Expression getRight() {
		return right;
	}
}