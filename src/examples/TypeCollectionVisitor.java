package examples;
import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

/**
 * this visitor is a simple example of how to traverse an SST with a visitor. It
 * simply collects all types that it finds in the SST.
 * 
 * This extends one of the two abstract implementations of the
 * {@link ISSTNodeVisitor} interface. The {@link AbstractTraversingNodeVisitor}
 * automatically traverses the whole syntax tree and you can selectively
 * override required methods.
 * 
 * The other implementation is {@link AbstractThrowingNodeVisitor}, which forces
 * you overwrite all methods to ensure you did not forget anything.
 */
public class TypeCollectionVisitor extends AbstractTraversingNodeVisitor<Set<ITypeName>, Void> {

	@Override
	public Void visit(IVariableDeclaration stmt, Set<ITypeName> seenTypes) {
		// for a variable declaration you can access the referenced type
		ITypeName type = stmt.getType();
		// let's put it into the index
		seenTypes.add(type);
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration decl, Set<ITypeName> seenTypes) {
		// access the fully-qualified name of this method
		IMethodName name = decl.getName();

		// store the return type
		seenTypes.add(name.getReturnType());

		// iterate over all parameters of this method
		for (IParameterName param : name.getParameters()) {
			// and store the simple names
			seenTypes.add(param.getValueType());
		}

		// now continue with the traversal, implemented in the base class
		return super.visit(decl, seenTypes);
	}

	// ... there are many other visit methods that you should override to handle
	// different code elements, but I guess, you get the idea now. :)
}