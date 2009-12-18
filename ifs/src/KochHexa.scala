// Koch hexagon
package code4joy.scala

import java.awt._
import scala.actors.Actor._

object KochHexa
{
	val maxIter  = 30
	val maxPt       = 10000000
	val numSamplePt = 100000
	//
	val imgWidth  = 1024*3
	val imgHeight = 1024*3
	val imgType   = "png"
	//
	var ratio     = 1024*3./4.
	var offsetX   = imgWidth/2
	var offsetY   = imgHeight/2
	//
	//
	val picFile   = PictureFile(imgWidth,imgHeight,imgType)
	//
	def main(param : Array[String]) = {
		
		def calcMinMax() : ((Double,Double),(Double,Double)) = {
			var minX =  99999999999.
			var maxX = -99999999999.
			var minY =  99999999999.
			var maxY = -99999999999.
			//
			val ifs = IFSChaosGame( IFSKochHexa )
			ifs.generate(numSamplePt,maxIter) { pt:(Double,Double)=>
				val (x,y) = pt
				if ( minX > x ) minX = x
				if ( minY > y ) minY = y
				if ( maxX < x ) maxX = x
				if ( maxY < y ) maxY = y
			}
			//
			( (minX,minY),(maxX,maxY) )
		}
		//
		def generateFile   () = {
			val fileName = "koch_hexa.png"
			println("Generating : " + fileName)
			val ifs = IFSChaosGame( IFSKochHexa )
			picFile.create(fileName){ g:Graphics2D =>
				ifs.generate(maxPt,maxIter) { pt:(Double,Double)=>
					//
					// plot it
					val (x,y)   = pt
					val (ix,iy) = (              (x*ratio).asInstanceOf[Int] + offsetX, 
								    imgHeight -( (y*ratio).asInstanceOf[Int] + offsetY ) )
					//println (ix,iy)
					g.drawLine( ix , iy , ix , iy )
				}
			}
			println(".... Done :" + fileName)
		}
		
		println ("hello graphics! Koch hexagon\n")
		//
		val ((minX,minY),(maxX,maxY)) = calcMinMax(); 
		//
		val width  = Math.abs(maxX - minX)
		val height = Math.abs(maxY - minY)
		//
		// println(width)
		// println(height)
		//
		if( width>height ){
			ratio   = imgWidth/width
			offsetX = -(ratio * minX).asInstanceOf[Int]
			offsetY = -(ratio * minY).asInstanceOf[Int]
		}
		else{
			ratio = imgHeight/height
			offsetX = -(ratio * minX).asInstanceOf[Int]
			offsetY = -(ratio * minY).asInstanceOf[Int]
		}
		//
		generateFile()
		//
	}
}
