import scala.collection.immutable._
import java.awt.{Graphics2D,Color}
import Math.{abs,min,max,log,sqrt,pow}

abstract class ChaosParameter[ T<:ChaosStream with ChaosStreamCanvas ]
{
	val imgType      :String = "png"
	val colorBG      :Color  = Color.BLACK
	//
	val dropIter     :Int    = 1000 
	val maxIter      :Int    = 1000 
	//
	var scaleFactor  :Double = 0.0  
	var numRing      :Int    = 5    
	var numTrajectoryPerRing = 0    
	//
	val gammaCorrection = 2 // gamma correction
	var samplingDegree  = 1 // sampling degree
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
		case "poor" =>   2
		case "low"  =>  10
		case "high" =>  30
		case _      =>  10
	}
	val paramSamplingDegree:(String)=>Int = {
		case "poor" => 1
		case "low"  => 2
		case "high" => 2
		case _      => 2
	}
	
	def parse(cmdParam:Array[String]) : Unit = {
		scaleFactor          = paramScaleFactor(cmdParam(0))
		numTrajectoryPerRing = paramNumTrajectoryPerRing(cmdParam(0))
		samplingDegree       = paramSamplingDegree(cmdParam(0))
		//
		(cmdParam drop 1).toList match {
		case "ow" :: Nil =>
			print("[overwrite only]")
			// overwriting mode
			fileFilter={_.exists}
			
		case _ => 
		}
	}
	//
	def doMain(cmdParam : Array[String]) : Unit = {
		parse(cmdParam)
		//
		val numTrajectory = numRing*numTrajectoryPerRing
		//
		setup()
		//
		println(
			"scaleFactor="          +scaleFactor         .formatted("%f")+
			",numTrajectoryPerRing="+numTrajectoryPerRing.formatted("%d")+
			",numRing="             +numRing             .formatted("%d")+
			",samplingDegree="      +samplingDegree      .formatted("%d")+
			",gammaCorrection="     +gammaCorrection     .formatted("%d"))
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
				val dataGeom = Geometry( p.calcMinMax(numTrajectory)(dropIter,maxIter) )
				val scale      = scaleFactor*1000
				//
				val freqLimit  = 1000 // limit fo freq
				//
				val geom =	if ( dataGeom.size.x > dataGeom.size.y )
								(	scale                       .asInstanceOf[Int] ,
									(scale/dataGeom.aspectRatio).asInstanceOf[Int] )
							else
								(	(scale*dataGeom.aspectRatio).asInstanceOf[Int] ,
									scale                       .asInstanceOf[Int] )
				//
				val histgram = new Array[Array[Double]](geom._1,geom._2)
				var maxFreq = 0.0
				var canvas:PictureFile with Canvas = null
				//
				print("Generating : " + filename ) // do not put newline
				if( geom._1 > 1 && geom._2 > 1 ){
					canvas = new PictureFile(file,geom,imgType,colorBG) with Canvas
					//
					//
					p.generateCanvasPoints(numTrajectory)(dropIter,maxIter)(canvas,dataGeom) {
						(count:Int,p:Vector[Int]) =>
							val freq =	if (count<0){
											val i = count + dropIter
											i.asInstanceOf[Double] / dropIter.asInstanceOf[Double]
										}
										else {
											log(Math.E + count)
										}
							//
							val hist = ( histgram(p.x)(p.y) + freq )*0.5 
							histgram(p.x)(p.y) = hist
							maxFreq = max( hist , maxFreq)
					}
				}
				if(maxFreq<=0){
					println(" ... no data")
				}
				else{
					print("[maxFreq="+maxFreq+"]")
					//
					if( maxFreq > freqLimit ){
						println(" ...too simple")
					}
					else {
						canvas.paint{ g:Graphics2D =>
							// inside loan of graphics object g
							for(ix<- 0 until geom._1 ; iy <- 0 until geom._2 ){
								var sumFreq   = 0.0
								var sumRatio  = 0.0
								val maxDistSq:Int = 2*samplingDegree*samplingDegree
								val rand = new java.util.Random
								//
								for(	dx <- -samplingDegree to samplingDegree ;
										dy <- -samplingDegree to samplingDegree ){
									val jx    = ix + dx
									val jy    = iy + dy
									if(	jx >= 0 && jx < geom._1 && 
										jy >= 0 && jy < geom._2 ) {
										val hist = histgram(jx)(jy)
										val distSq:Int = dx*dx + dy*dy
										val r   :Double = abs(0.5+distSq-maxDistSq).asInstanceOf[Double]/maxDistSq.asInstanceOf[Double]
										val ratio= r + 10*rand.nextDouble
										//println("dist,r,ratio="+(dist,r,ratio))
										//
										sumFreq  += ratio*hist
										sumRatio += ratio
									}
								}
								val avgFreq:Double=	if (sumRatio > 0) sumFreq / sumRatio
													else 0
								
								if( avgFreq>0 ){
									val ratio = avgFreq / maxFreq
									// val ratio = log(avgFreq) / log(maxFreq)
									// println(ratio)
									val alpha = pow(ratio,1.0/gammaCorrection)
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
