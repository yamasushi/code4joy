// Curlicue Fractal
// 2009-09-05

package code4joy.scala

import java.awt._

object Curlicue
{
	//val curlicueParamS = Math.sqrt(2);
	//val curlicueName   = "sqrt2"
	//val curlicueParamS = Math.Pi;
	//val curlicueName   = "pi"
	//val curlicueParamS = Math.exp(1);
	//val curlicueName   = "e"
	//val curlicueParamS = (1. + Math.sqrt(5))/2.;
	//val curlicueName   = "goldenratio"
	//val curlicueParamS = Math.log(2);
	//val curlicueName   = "ln2"
	//val curlicueParamS = Math.Pi * Math.log(2);
	//val curlicueName   = "pi_x_ln2"
	//
	//val curlicueParamS = 9.80665
	//val curlicueName   = "G"
	//
	val numFile   = 100
	val maxPt     = 1000000
	//
	val imgWidth  = 1024*2
	val imgHeight = 1024*2
	val imgType   = "png"
	//
	def main(param : Array[String]) = {
		val rand  = new java.util.Random
		//
		for(i <- 0 until numFile)
			generate( rand.nextDouble() ) 
	}
	
	def generate(curlicueParamS:Double) = {
		val curlicueName   = curlicueParamS.formatted("%8.7f")
		def curlicuePoint(counter:Int , pt:(Double,Double) , phi:Double , theta:Double)(op:((Double,Double))=>Unit) : Unit  = {
			op(pt)
			if( counter <= 0 ){
				()
			}
			else {
				curlicuePoint( counter-1 , 
							( pt._1 + Math.cos(phi) , pt._2 + Math.sin(phi) ) , 
							(theta + phi) % (Math.Pi*2) , 						
							(theta + (Math.Pi*2)*curlicueParamS ) % (Math.Pi*2) )(op)
			}
		}
		
		def calcMinMax() : ((Double,Double),(Double,Double)) = {
			var minX =  99999999999.
			var maxX = -99999999999.
			var minY =  99999999999.
			var maxY = -99999999999.
			//
			curlicuePoint(maxPt,(0,0),0,0){ pt:(Double,Double)=>
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
		var ratio     = 0.
		var offsetX   = 0
		var offsetY   = 0
		//
		def generateFile   () = {
			val fileName = "curlicue_"+curlicueName+"."+imgType
			println("Generating : " + fileName)
			//
			var lastPt:(Int,Int) = null
			//
			val picFile   = PictureFile(imgWidth,imgHeight,imgType)
			picFile.create(fileName){ g:Graphics2D =>
				curlicuePoint(maxPt,(0,0),0,0){ currentPt:(Double,Double)=>
					// plot it
					val (x,y)   = currentPt
					val (ix,iy) = (              (x*ratio).asInstanceOf[Int] + offsetX, 
								    imgHeight -( (y*ratio).asInstanceOf[Int] + offsetY ) )
					//
					if (lastPt == null ) {
						lastPt = (ix,iy)
					}
					//
					//println (ix,iy)
					g.drawLine( lastPt._1 , lastPt._2 , ix , iy )
					lastPt = (ix,iy)
				}
			}
			println(".... Done :" + fileName)
		}
		
		//println ("hello graphics! curlicue\n")
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
