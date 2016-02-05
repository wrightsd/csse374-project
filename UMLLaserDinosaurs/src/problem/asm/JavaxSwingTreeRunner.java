package problem.asm;

import java.io.IOException;

public class JavaxSwingTreeRunner {

	public static void main(String[] args) throws IOException {
		String[] arguments = { "javax.swing.tree.MutableTreeNode", "javax.swing.tree.RowMapper",
				"javax.swing.tree.TreeCellEditor", "javax.swing.tree.TreeCellRenderer", "javax.swing.tree.TreeModel",
				"javax.swing.tree.TreeNode", "javax.swing.tree.TreeSelectionModel",
				"javax.swing.tree.DefaultMutableTreeNode", "javax.swing.tree.DefaultTreeCellEditor",
				"javax.swing.tree.DefaultTreeCellRenderer", "javax.swing.tree.DefaultTreeModel",
				"javax.swing.tree.DefaultTreeSelectionModel", "javax.swing.tree.FixedHeightLayoutCache",
				"javax.swing.tree.TreePath"};
		DesignParser.parse(arguments, "./output/JavaxSwingTree.txt", "uml");
	}

}
