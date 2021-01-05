package com.squarespace.interview.findpoint

import java.awt.Point

object FindPointSolutionKt {

    //wrapper class to include parent id which we use in post-processing
    class NodeEx constructor(val node:Node,val parent:String)

    private lateinit var result:MutableList<String>
    private lateinit var root:Node
    private lateinit var candidateNodes:MutableList<NodeEx>

    /**
     * Given a root [Node] of a view hierarchy, provide an ordered list of
     * [Node IDs][Node.id] representing the path through the hierarchy where
     * the given [Point] can be located.
     *
     * @param rootNode Top-level node of the view hierarchy.
     * @param toFind An x,y coordinate to be located.
     * @return An ordered list of [Node.id]
     */
    @JvmStatic
    fun findPathToNode(rootNode: Node, toFind: Point): List<String> {
        result= mutableListOf()
        candidateNodes= mutableListOf()
        root=rootNode
        //negative results are illegal and points outside origin should not be considered
        // so result empty list in those cases
        if((toFind.x < 0 || toFind.y < 0) or !inPath(rootNode, toFind)) {
            return result
        }else{
            searchTreeForMatchingPath(toFind, rootNode,0,0)
        }

        return result
    }

    /**
    * @param toFind point to search for in current rect & child nodes (if any)
     * @param searchNode current searchNode to search
     * @param offsetX cumulative relative offset relative to parent
     * @param offsetY cumulative relative offset relative to parent
     * */
    private fun searchTreeForMatchingPath(toFind: Point, searchNode: Node, offsetX:Int, offsetY:Int):Boolean{
        //loop over children and recurse
        if(searchNode.children.size>0) {
            var found:Node?=null
            for(node in searchNode.children){
                //offsets are accumulated at every level in the hierarchy
                val offX = offsetX + searchNode.left
                val offY = offsetY + searchNode.top
                if(searchTreeForMatchingPath(toFind, node, offX,offY)){
                    //matches/true values at a higher child index
                    // overwrite those at a lower index with every loop iteration
                    found=node
                }
            }
            if(found!=null){
                val nodeEx=NodeEx(found,searchNode.id)
                candidateNodes.add(nodeEx)
            }
        }
        if(inRect(searchNode, toFind,offsetX,offsetY)){
            if (searchNode.id == root.id) {
                postProcessing(searchNode)
            }
            return true
        }
        return false
    }

    /**
     * Function processes the candidate node list producing only top level one path
     * and discarding any other paths (the unprocessed candidate nodes list can include extraneous nodes)
     */
    private fun postProcessing(searchNode: Node) {
        if (searchNode.id == root.id) {
            result.clear()
            candidateNodes.add(NodeEx(searchNode,  ""))
            candidateNodes.reverse()
            var parentId = ""
            for ((index, nodEx) in candidateNodes.withIndex()) {
                if (index == 0) {
                    result.add(nodEx.node.id)
                }
                if (index > 0) {
                    //if this node does not have the previous node as
                    // a parent id then it's a branch with a lower index
                    // so this node and everything after can be ignored
                    // thus we break and exit the loop
                    if (nodEx.parent != parentId) {
                        break
                    } else {
                        result.add(nodEx.node.id)
                    }
                }
                parentId = nodEx.node.id
            }
        }
    }

    /**
     * Function tests whether point is inside or outside the orioin of the rect/node
    * @param node the node representing the rect to test for inclusion
    * @param toFind the point to find in the rect
    */
    private fun inPath(node: Node, toFind: Point):Boolean{
        return (toFind.x >=node.left) && (toFind.y >=node.top)
    }

    /**
     * Function performs the Rect hit-test for the respective point
     * @param node the node representing the rect to test for inclusion
     * @param toFind the point to find in the rect
     * @param offsetX the cumulative X-axis offset if the node has parent node(s)
     * @param offsetY the cumulative Y-axis offset if the node has parent node(s)
     */
    private fun inRect(node: Node, toFind: Point, offsetX:Int, offsetY:Int):Boolean{
        val x1= node.left + offsetX
        val y1=node.top + offsetY

        //diagonally opposite corner
        val x2=x1 + node.width
        val y2=y1 + node.height

        //is the point located in the bounds of the rect
       return (toFind.x in x1 until x2 && toFind.y in y1 until y2)
    }
}
