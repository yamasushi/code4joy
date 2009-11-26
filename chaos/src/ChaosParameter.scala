import scala.collection.immutable._
import java.awt.{Graphics2D,Color}
import Math.{abs,min,max,log,sqrt,pow}

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
	var fileFilter:(java.io.File)=>Boolean = {!_.exists}
	//
	val paramScaleFactor:(String)=>Double = {
		case "poor" => 0.5
		case "low"  => 1.0
		case "high" => 3.0
		case _      => 1.0
	}
	val paramNumTrajectoryPerRing:(String)=>Int = {
		case "poor" => 100
		case "low"  => 100
		case "high" => 500
		case _      => 100
	}
	
	def parse(cmdParam:Array[String]) : Unit = {
		scaleFactor          = paramScaleFactor(cmdParam(0))
		numTrajectoryPerRing = paramNumTrajectoryPerRing(cmdParam(0))
		
		(cmdParam drop 1).toList match {
		case "ow" :: Nil =>
			print("[overwrite only]")
			// overwriting mode
			fileFilter={_.exists}
			
		case _ => 
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
			if fileFilter(file) ) {
			
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
				val width      = abs( maxx - minx ) + 0.000001
				val height     = abs( maxy - miny ) + 0.000001
				val aspectRatio= width/height
				val scale      = scaleFactor*1000
				//
				val freqLimit  = 1000 // limit fo freq
				val gamma      = 2    // gamma correction
				val degree     = 1    // sampling degree
				//
				val geom =	if (width>height)
								(	scale              .asInstanceOf[Int] ,
									(scale/aspectRatio).asInstanceOf[Int] )
							else
								(	(scale*aspectRatio).asInstanceOf[Int] ,
									scale              .asInstanceOf[Int] )
				//
				if( geom._1 > 1 && geom._2 > 1 ){
					val canvas = new PictureFile(file,geom,imgType,colorBG) with Canvas
					print("Generating : " + filename ) // do not put newline
					//
					val histgram = new Array[Array[Double]](geom._1,geom._2)
					//
					var maxFreq = 0.0
					p.generateCanvasPoints(numTrajectory)(dropIter,maxIter)(canvas)(minmax) {
						(count:Int,p:(Int,Int)) =>
							val freq =	if (count<0)	histgram(p._1)(p._2) + 1
										else			histgram(p._1)(p._2) + 1
							//
							histgram(p._1)(p._2) = freq
							maxFreq = max( freq , maxFreq)
					}
					//
					assert( maxFreq>0 )
					print("[maxFreq="+maxFreq+"]")
					//
					if( maxFreq > freqLimit ){
						println(" ...too simple")
					}
					else {
						canvas.paint{ g:Graphics2D =>
							// inside loan of graphics object g
							for(ix<- 0 until geom._1 ; iy <- 0 until geom._2 ){
								var sumFreq = 0.0
								var num = 0
								for(	jx <- (ix-degree) to (ix+degree) ;
										jy <- (iy-degree) to (iy+degree) ){
									if(	jx >= 0 && jx < geom._1 && 
										jy >= 0 && jy < geom._2 ) {
										//
										sumFreq += histgram(jx)(jy)
										num += 1
									}
								}
								val avgFreq:Double=	if (num >= 1) sumFreq / num.asInstanceOf[Double]
													else 0
								
								if( avgFreq>1 ){
									// val ratio0 = avgFreq / maxFreq
									val ratio = log(avgFreq) / log(maxFreq)
									//println(ratio)
									val alpha = pow(ratio,1.0/gamma)
									//
									val icol = min( (255*alpha).asInstanceOf[Int] , 255 )
									//
									g.setColor( new Color(icol,icol,icol) )
									g.drawLine( ix , iy , ix , iy )
								}
							}
							print(" ... Writing") // do not put newline
						}
						println(" ... Done")
					}
				}
			}
		}
	}
	//
	def add(a:T) : Unit = {
		param = param enqueue a
	}
}
