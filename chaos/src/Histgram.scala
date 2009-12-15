import java.awt.{Graphics2D,Color}
import Math.{abs,min,max,log,sqrt,pow,exp}
import scala.collection.immutable._


case class Histgram(	imgGeom :Geometry[Int]    ,
						dataGeom:Geometry[Double] ,
						cellUnit:Double           ,
						onUpdate:Double => Unit   )
{
	private val rand     = new java.util.Random
	//
	private val rxCell     = cellUnit
	private val ryCell     = sqrt(3.0)*cellUnit
	private val widthCell  = (imgGeom.size.x / rxCell + 0.5).asInstanceOf[Int]
	private val heightCell = (imgGeom.size.y / rxCell + 0.5).asInstanceOf[Int]
	private val histgram  = new Array[Array[Float]](widthCell,heightCell)
	//
	val canvasTransform=Geometry.transform(imgGeom,dataGeom)
	//
	def update(p:Vector[Double] , v:Double) : Double = {
		val pt = canvasTransform(p)
		val ix = ( (pt.x / rxCell ) + 0.5 ).asInstanceOf[Int]
		val iy = ( (pt.y / ryCell ) + 0.5 ).asInstanceOf[Int]
		//
		onUpdate(v)
		val result:Double = histgram.updateCell((ix,iy) , v.asInstanceOf[Float])
		result
	}
	//
	def foreach ( op: (Array[Int],Array[Int],Float)=>Unit ) : Unit = {
		//
		for (iy <- 0 until heightCell-1 ) {
			val y = iy.asInstanceOf[Double]*ryCell
			val ixRange =	if ( iy%2==0 ) new Range( 0 , widthCell-1 , 6)
							else           new Range( 3 , widthCell-1 , 6)
			//
			val yData=Array(	(y          + 0.5).asInstanceOf[Int] ,
								(y + ryCell + 0.5).asInstanceOf[Int] ,
								(y + ryCell + 0.5).asInstanceOf[Int] ,
								(y          + 0.5).asInstanceOf[Int] ,
								(y - ryCell + 0.5).asInstanceOf[Int] ,
								(y - ryCell + 0.5).asInstanceOf[Int] )
			for (ix<-ixRange) {
				val x = ix.asInstanceOf[Double]*rxCell
				//
				val xData=Array(	(x + 2*rxCell + 0.5).asInstanceOf[Int] , 
									(x +   rxCell + 0.5).asInstanceOf[Int] ,
									(x -   rxCell + 0.5).asInstanceOf[Int] ,
									(x - 2*rxCell + 0.5).asInstanceOf[Int] ,
									(x -   rxCell + 0.5).asInstanceOf[Int] ,
									(x +   rxCell + 0.5).asInstanceOf[Int] )
				op( xData , yData , histgram((ix,iy)) )
			}
		}
	}
	//
	def smoothing( condOp:Float=>Boolean ) : Unit = {
		for(	jx <- 0 to widthCell -1 ;
				jy <- 0 to heightCell-1 ) {
			val h = histgram((jx,jy))
			if( condOp(h) ) {
				val nb = Lattice.neighbor((jx,jy))
				val histNb = ( (0.0f /: nb){(acc,v) => acc+histgram(v) } ) / nb.length
				//
				//onUpdate(histNb)
				histgram.updateCell( (jx,jy) , histNb )
			}
		}
	}
	//
	def rendering(op:(Array[Int],Array[Int],Double)=>Unit) : Unit = {
		//
		// smoothing( { _ >= 1.0f } ) // only brighter spot
		// smoothing( { _ >= 1.0f } ) // only brighter spot
		// smoothing( { _ < 1.0f  } ) // only darker spot
		// smoothing( { _ < 1.0f  } ) // only darker spot
		smoothing( { _:Float=>true } )
		//smoothing( { _ == 0.0f } )
		//smoothing( { _ == 0.0f } )
		//
		this.foreach { (xs,ys,h) =>
			if( h>0 &&
				xs.forall{ t => t>=0 && t< imgGeom.size.x } &&
				ys.forall{ t => t>=0 && t< imgGeom.size.y } ) {
				op(xs,ys,h)
			}
		}
	}
	
	class ImageBuffer( buffer:Array[Array[Float]] )
	{
		def indexIsValid( ip:(Int,Int) ) : Boolean = {
			val (ix,iy) = ip
			//
			if ( ix < 0 ) return false
			if ( iy < 0 ) return false
			//
			if ( ix >= buffer.length     ) return false
			if ( iy >= buffer(ix).length ) return false
			//
			return true
		}
		
		def apply( ip:Vector[Int] ) : Float = apply((ip.x,ip.y))
		def apply( ip:(Int,Int) ) : Float = {
			if (indexIsValid(ip)) buffer(ip._1)(ip._2)
			else 0.0f
		}
		
		def updateCell( ip:(Int,Int) , v:Float ) : Float = {
			if (indexIsValid(ip)) {
				val h:Float = ( buffer(ip._1)(ip._2) + v ) * 0.5f
				buffer(ip._1)(ip._2) = h
				h
			}
			else {
				0.0f
			}
		}
	}
	//
	implicit def a2ib( a:Array[Array[Float]] ):ImageBuffer = new ImageBuffer(a)
}
