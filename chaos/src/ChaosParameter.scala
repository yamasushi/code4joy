import scala.collection.immutable._
import java.awt.{Graphics2D,Color}
import Math.{abs,min,max,log,sqrt}

abstract class ChaosParameter[ T<:ChaosImageParam with ChaosStreamCanvas ]
{
	val imgType      :String = "png"
	val colorBG      :Color  = Color.BLACK
	//
	val dropIter     :Int    = 1000 
	val maxIter      :Int    = 1000 
	//
	var scaleFactor  :Double = 0.0  
	var numRing      :Int    = 3    
	var numTrajectoryPerRing = 0    
	//
	def setup() : Unit
	//
	private var param = new Queue[T]
	//
	def parse(cmdParam:Array[String]) : Unit = {
		cmdParam.toList match {
		case "poor" :: Nil =>
			scaleFactor          =   0.5
			numTrajectoryPerRing =  10
			
		case "low" :: Nil =>
			scaleFactor          =   1.
			numTrajectoryPerRing = 100
			
		case "high" :: Nil =>
			scaleFactor          =   3.
			numTrajectoryPerRing = 500
			
		case str0 :: str1 :: str2 :: Nil =>
			scaleFactor          = str0 .toDouble
			numTrajectoryPerRing = str1 .toInt
			numRing              = str2 .toInt
			
		case str0 :: str1 :: Nil =>
			scaleFactor          = str0 .toDouble
			numTrajectoryPerRing = str1 .toInt
			
		case _ => exit(0)
		}
		println(
			"scaleFactor="          +scaleFactor         .formatted("%f")+
			",numTrajectoryPerRing="+numTrajectoryPerRing.formatted("%d")+
			",numRing="             +numRing             .formatted("%d"))
	}
	//
	def doMain(cmdParam : Array[String]) : Unit = {
		parse(cmdParam)
		//
		val numTrajectory = numRing*numTrajectoryPerRing
		//
		setup()
		//
		for( p <- param if p.isValid ;
			filename = p.chaosName +"."+imgType ;
			file = new java.io.File(filename) ;
			if (!file.exists ) ) {
			
			if ( p.isDivergence(100)(maxIter) ) {
				println("unstable :"+p.chaosName)
			}
			else {
				//val lyapunov = Lyapunov.calc(p.chaosSystem,10000,(0.0,0.0))
				//print("lyapunov["+lyapunov.formatted("%3f")+"]")
				//
				//
				val minmax = p.calcMinMax(numTrajectory)(dropIter,maxIter)
				val (minXY,maxXY) = minmax
				val (minx ,miny)  = minXY
				val (maxx ,maxy)  = maxXY
				val width   = abs( maxx - minx ) + 0.000001
				val height  = abs( maxy - miny ) + 0.000001
				//
				val geom =	if (width>height)
								(	(scaleFactor* 1600).asInstanceOf[Int] ,
									(scaleFactor*  900).asInstanceOf[Int] )
							else
								(	(scaleFactor*  900).asInstanceOf[Int] ,
									(scaleFactor* 1600).asInstanceOf[Int] )
				//
				val canvas = new PictureFile(file,geom,imgType,colorBG) with Canvas
				print("Generating : " + filename ) // do not put newline
				//
				canvas.paint{ g:Graphics2D =>
					// inside loan of graphics object g
					p.generateCanvasPoints(numTrajectory)(dropIter,maxIter)(canvas)(minmax)(p.drawPoint(g))
					print(" ... Writing") // do not put newline
				}
				println(" ... Done")
			}
		}
	}
	//
	def add(a:T) : Unit = {
		param = param enqueue a
	}
}
