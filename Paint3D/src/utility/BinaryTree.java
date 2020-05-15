package utility;

import java.util.ListIterator;

public class BinaryTree implements Tree {
	
	private TreeNode currentNode=null;
	private TreeNode rootNode=null;

	@Override
	public Object element(TreeNode v) {
		// TODO Auto-generated method stub
		return v.getElement();
	}

	@Override
	public TreeNode root() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeNode parent(TreeNode v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<TreeNode> children(TreeNode v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf(TreeNode v) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRoot(TreeNode v) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int level(TreeNode v) {
		if(this.isRoot(v))
			return 0;
		else
			return 1 + level(this.parent(v));
	}

	public TreeNode getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(TreeNode currentNode) {
		this.currentNode = currentNode;
	}

	public TreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(TreeNode rootNode) {
		this.rootNode = rootNode;
	}
	
	public void addRight(TreeNode node) {
		currentNode.setRightChild(node);
	}
	
	public void addLeft(TreeNode node) {
		currentNode.setLeftChild(node);
		
	}

}
