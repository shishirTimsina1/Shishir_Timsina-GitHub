public class BST<T extends Comparable<T>> implements BST_Inter<T>{
	private BTNode root;
	private BTNode parent;
	private StringBuilder str = new StringBuilder();
	private StringBuilder str2 = new StringBuilder();
	//public static BTNode root;
	
	/**
	 * Add a new key to the BST
	 *
	 * @param 	key Generic type value to be added to the BST
	 */
	public void put(T key){
		BTNode bn = new BTNode(key);
		if(root == null){
			root = bn;
			return;
		}
		BTNode parent = null, x = root;
		while(x!=null){
			parent = x;
			int cmp = key.compareTo((T)x.getKey());
			if(cmp < 0)
				x = x.getLeft();
			else if(cmp > 0)
				x = x.getRight();
			else
				return;
		}
		int cmp = key.compareTo((T)parent.getKey());
		if(cmp<0)
			parent.setLeft(bn);
		else
			parent.setRight(bn);
		assert check();
	}
	
	/**
	 * Check if the BST contains a key
	 *
	 * @param	key Generic type value to look for in the BST
	 *
	 * @return	true if key is in the tree, false otherwise
	 */
	 
	public boolean contains(T key){
		BTNode temp = root;
		while(temp!=null){
			int cmp = key.compareTo((T)temp.getKey());
			if (cmp < 0)
				temp = temp.getLeft();
			else if(cmp > 0)
				temp = temp.getRight();
			else return true;
		}
		return false;
		
	}
	
	/**
	 * Remove a key from the BST, if key is present
	 * 
	 * @param	key Generic type value to remove from the BST
	 */
	public void delete(T key){
		BTNode temp = new BTNode(key);
		if(temp == null)
			return;
		
		BTNode[] nodeDelete = this.findNode(key);
		if(nodeDelete[1] == null)
			return;
		if(!hasSubtree(nodeDelete[1])){
			deleteLeaf(nodeDelete[1], nodeDelete[0]);
			return;
		}

		if(nodeDelete[1].getLeft() == null){
			System.out.println("left is null");
			if(nodeDelete[1] == root){
				root = root.getRight();
				return;
			}
			if(nodeDelete[0].getRight() == nodeDelete[1]){
				nodeDelete[0].setRight(nodeDelete[1].getRight());
			}
			else
				nodeDelete[0].setRight(nodeDelete[1].getRight());
			return;

		}

		if(nodeDelete[1].getRight() == null){
			System.out.println("right is null");
			if(nodeDelete[1] == root){
				root = root.getLeft();
				return;
			}
			if (nodeDelete[0].getLeft() == nodeDelete[1]){
				nodeDelete[0].setLeft(nodeDelete[1].getLeft());
			}
			else 
				nodeDelete[0].setRight(nodeDelete[1].getLeft());
			return;
		}
		if(nodeDelete[1] == root){
			if(nodeDelete[1].getRight().getLeft() == null){
				BTNode p = nodeDelete[1].getRight();
				nodeDelete[1].setKey(p.getKey());
				nodeDelete[1].setRight(p.getRight());
				assert check();
				return;
				
			}
			return;
		}
		
		BTNode rightSmallest = min(nodeDelete[1].getRight());
		BTNode[] removeLeaf = findNode((T)rightSmallest.getKey());
		
		deleteLeaf(rightSmallest, removeLeaf[0]);
		nodeDelete[1].setKey(rightSmallest.getKey());

		
		assert check();
	}
	
	private void deleteLeaf(BTNode n, BTNode p){
		if(n == root){
			root = null;
		}
		if(p.getLeft() == n){
			p.setLeft(null);
		}
		else if(p.getRight() == n){
			p.setRight(null);
		}
		assert check();
	}
	
	/**
	 * Find the min key from the BST, if key is present
	 * 
	 * @param	BTNode root
	 */
	private BTNode min(BTNode n){
		if(n.getLeft() == null)
			return n;
		else 
			return min(n.getLeft());

	}
	/**
	 * See if a given node has a subtree attached
	 * 
	 * @param	BTNode n, node to check on
	 */
	public boolean hasSubtree(BTNode n){
		BTNode left = n.getLeft();
		BTNode right = n.getRight();
		if(left == null && right == null){
			return false;
		}
		return true; 
	}
	
	/**
	 * Find and return the node given the key
	 * Returns an array that contains the parent in the first index
	 *  child/ found node in the second index
	 * @param	key Generic type value to find
	 */
	public BTNode[] findNode(T key){
		BTNode[] arr = new BTNode[2];
		BTNode parent = root;
		BTNode temp = root;
		BTNode prev = root;
		while(temp != null){
			int cmp = key.compareTo((T)temp.getKey());
			if(cmp < 0){
				prev = temp;
				temp = temp.getLeft();
			}
			else if(cmp > 0){
				prev = temp;
				temp = temp.getRight();
			}
			else{
				parent = prev;
				arr[0] = parent;
				arr[1] = temp;
				System.out.printf("found key %d \n", (int) temp.getKey());
				return arr;
			}
		}
		return arr;
	}
	
	/**
	 * Determine the height of the BST
	 *
	 * <p>
	 * A single node tree has a height of 1, an empty tree has a height of 0.
	 *
	 * @return	int value indicating the height of the BST
	 */
	public int height(){
		int height = height(root);
		return height;
	}
	public int height(BTNode r){
		BTNode temp = r;
		if(temp == null)
			return 0;
		int lTree = height(temp.getLeft());
		int rTree = height(temp.getRight());
		if(lTree > rTree)	return (lTree +1);
		else 	return (rTree+1);

	}
	
	/**
	 * Determine if the BST is height-balanced
	 *
	 * <p>
	 * A height balanced binary tree is one where the left and right subtrees
	 * of all nodes differ in height by no more than 1.
	 *
	 * @return true if the BST is height-balanced, false if it is not
	 */
	public boolean isBalanced(){
		return isBalanced(root);
	}
	public boolean isBalanced(BTNode n){
		if(n == null)
			return true;
		int leftHeight, rightHeight, diff;
		leftHeight = height(n.getLeft());
		rightHeight = height(n.getRight());
		diff = Math.abs(leftHeight - rightHeight);
		if(diff <= 1){
			if(isBalanced(n.getLeft()) && isBalanced(n.getRight()))
				return true;
		}
		return false;


	}
	/**
	 * Produce a ':' separated String of all keys in ascending order
	 *
	 * <p>
	 * Perform an in-order traversal of the tree and produce a String
	 * containing the keys in ascending order, separated by ':'s.
	 * 
	 * @return	TBD if kept
	 */
	public String inOrderTraversal(){
		str.setLength(0);
		inorderrec(root);
		String ret = str.toString();
		ret = ret.substring(0,ret.length()-1);
		assert check();
		return ret;
	}
	public void inorderrec(BTNode r){
		if (r == null) return;
		inorderrec(r.getLeft());
		str.append(String.valueOf(r.getKey()) + ":");
		inorderrec(r.getRight());
		assert check();
		
	}

	/**
	 * Produce String representation of the BST
	 * 
	 * <p>
	 * Perform a pre-order traversal of the BST in order to produce a String
	 * representation of the BST. The reprsentation should be a comma separated
	 * list where each entry represents a single node. Each entry should take
	 * the form: *type*(*key*). You should track 4 node types:
	 *     `R`: The root of the tree
	 *     `I`: An interior node of the tree (e.g., not the root, not a leaf)
	 *     `L`: A leaf of the tree
	 *     `X`: A stand-in for a null reference
	 * For each node, you should list its left child first, then its right
	 * child. You do not need to list children of leaves. The `X` type is only
	 * for nodes that have one valid child.
	 * 
	 * @return	String representation of the BST
	 */
	public String serialize(){
		serialize(root);
		String ret = str2.toString();
		ret = ret.substring(0,ret.length()-1);

		return ret;
	}
	private void serialize(BTNode r){
		if (r == null) return;
		if(r == root){
			String o = "R("+String.valueOf(r.getKey())+"),";
			str2.append(o);
		}
		if(!hasSubtree(r)){
			String l = "L("+String.valueOf(r.getKey())+"),";
			str2.append(l);
		}
		if(hasSubtree(r) && r!=root){
			String i = "I("+String.valueOf(r.getKey())+"),";
			str2.append(i);
		}
		if(r.getLeft() == null && hasSubtree(r)){
			String x = "X(NULL),";
			str2.append(x);
		}

		
		serialize(r.getLeft());
		if(r.getRight() == null && hasSubtree(r)){
			String x = "X(NULL),";
			str2.append(x);
		}
		serialize(r.getRight());
	}
	
	/**
	 * Modifies the BST into one that is reversed (i.e., left children
	 * hold keys greater than the current key, right children hold keys less
	 * than the current key).
	 *
	 * @return	 BST reversed
	 */
	public BST_Inter<T> reverse(){
		this.reverse(root);
		str2.setLength(0);
		return (BST_Inter)this;
	}
	public BTNode reverse(BTNode r){
		//root = r;
		if(r == null)
			return r;
		BTNode temp = r.getLeft();
		r.setLeft(r.getRight());
		r.setRight(temp);

		reverse(r.getLeft());
		reverse(r.getRight());
		return r;
	}
	/*************************************************************************
    *  Check integrity of BST data structure.
    ***************************************************************************/
    // MODIFIED FROM https://algs4.cs.princeton.edu/32bst/BST.java.html
	private boolean check() {
        if (!isBST())            System.out.println("Not in symmetric order");
        return true;
    }
    private boolean isBST() {
        return isBST(root, null, null);
    }
    private boolean isBST(BTNode x, T min, T max) {
        if (x == null) return true;
        if (min != null && x.getKey().compareTo(min) <= 0) return false;
        if (max != null && x.getKey().compareTo(max) >= 0) return false;
        return isBST(x.getLeft(), min, (T)x.getKey()) && isBST(x.getRight(), (T)x.getKey(), max);
    } 
	
	public static void main( String[] args ){
		//BST tree = new BST();
		String[] puts = {"a","g","z","d","c","f"};
		BST<String> t = new BST<String>();

		for (String i: puts) {
			t.put(i);
		}
		String resultI = t.inOrderTraversal();
		System.out.printf("\nSuccessfully built a tree with %d nodes!", puts.length);
		int height = t.height();
		System.out.printf("\nheight is %d \n", height);
		System.out.print(resultI);
		
		//////////////////MORE TESTS / FUNCTIONS////////////////////////////////////
		
	}	

}