package utility;

import java.util.ListIterator;

public interface Tree {
	Object element(TreeNode v);
	TreeNode root();
	TreeNode parent(TreeNode v);
	ListIterator<TreeNode> children(TreeNode v);
	boolean isLeaf(TreeNode v);
	boolean isRoot(TreeNode v);
}
