package com.squarespace.interview.findpoint

import java.awt.Point
import kotlin.math.abs

object FindPointSolutionKt {

    /**
     * Given a root [Node] of a view hierarchy, provide an ordered list of
     * [Node IDs][Node.id] representing the path through the hierarchy where
     * the given [Point] can be located.
     *
     * @param rootNode Top-level node of the view hierarchy.
     * @param toFind An x,y coordinate to be located.
     * @return An ordered list of [Node.id]
     */
    private var result= mutableListOf<String>()

    @JvmStatic
    fun findPathToNode(rootNode: Node, toFind: Point): List<String> {
        result= mutableListOf<String>()
        //negative results are illegal so result empty list
        if(toFind.x < 0 || toFind.y < 0){
            return result
        }else{
            if(!inPath(rootNode, toFind)){
                return result
            }else{
                searchTree(toFind, rootNode)
                result.reverse()
            }
        }

        return result
    }

    fun searchTree(toFind: Point, searchNode: Node, isChildNode: Boolean = false):Boolean{

        if(searchNode.children.size>0) {
            var found:String?=null
            for(node in searchNode.children){
                if(isInParentBounds(node,searchNode)
                        && searchTree(toFind, node, true)) {
                    found=node.id
                }
            }
            if(found!=null){
                result.add(found)
                result.add(searchNode.id)
                return false
            }
        }
        if(inRect(searchNode, toFind)){
            if(!isChildNode){
                result.add(searchNode.id)
            }
            return true
        }

        return false
    }

    fun inPath(node: Node, toFind: Point):Boolean{
        return (toFind.x >=node.left) && (toFind.y >=node.top)
    }

    fun inRect(node: Node, toFind: Point):Boolean{
        val x1= node.left
        val y1=node.top

        //diagonally opposite corner
        val x2=x1 + node.width
        val y2=y1 + node.height

        //is the point located in the bounds of the rect
       return (toFind.x >= x1 && toFind.x < x2 &&
                toFind.y >= y1 && toFind.y < y2)
    }

    fun inRect1(node: Node, toFind: Point):Boolean{
        //origin
        val x1= node.left
        val y1=node.top

        //top left
        val x2=x1
        val y2=y1 + node.height

        //diagonally opposite corner
        val x3=x1 + node.width
        val y3=y2

        //bottom right
        val x4=x3
        val y4=y1

        return checkSumOfTriangles(x1,y1,x2,y2,x3,y3,x4,y4,toFind.x,toFind.y)
    }

    fun isInParentBounds(child: Node, parent: Node):Boolean{
        val xCheck=(child.left + child.width)
        val yCheck=(child.top + child.height)
       return ( 0 < xCheck && xCheck <= parent.width) &&
           (0 < yCheck && yCheck <= parent.height)

    }


    fun checkSumOfTriangles(x1: Int, y1: Int, x2: Int, y2: Int,
                            x3: Int, y3: Int, x4: Int, y4: Int, x: Int, y: Int): Boolean {

        /* Calculate area of rectangle ABCD */
        val rectABCD: Float = area(x1, y1, x2, y2, x3, y3) +
                area(x1, y1, x4, y4, x3, y3)

        /* area of triangle PAB */
        val triPAB: Float = area(x, y, x1, y1, x2, y2)

        /* area of triangle PBC */
        val triPBC: Float = area(x, y, x2, y2, x3, y3)

        /* area of triangle PCD */
        val triPCD: Float = area(x, y, x3, y3, x4, y4)

        /* Calculate area of triangle PAD */
        val triPAD: Float = area(x, y, x1, y1, x4, y4)

        /* Check if sum of A1, A2, A3 and A4 is same as A */
        return rectABCD == triPAB + triPBC + triPCD + triPAD
    }

    fun area(x1: Int, y1: Int, x2: Int,
             y2: Int, x3: Int, y3: Int): Float {
        return abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0).toFloat()
    }
}
