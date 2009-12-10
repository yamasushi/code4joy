import scala.collection.immutable._
import java.awt.{Graphics2D,Color}
import Math.{abs,min,max,log,sqrt,pow}

abstract class ChaosParameter[ T<:ChaosStream with ChaosStream ]
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
		case "poor" => 0.1
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
		case "low"  => 1
		case "high" => 1
		case _      => 1
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
				val imgSize:Vector[Int]=if ( dataGeom.size.x > dataGeom.size.y )
											(	scale                       .asInstanceOf[Int] ,
												(scale/dataGeom.aspectRatio).asInstanceOf[Int] )
										else
											(	(scale*dataGeom.aspectRatio).asInstanceOf[Int] ,
												scale                       .asInstanceOf[Int] )
				//
				if( imgSize.x > 1 && imgSize.y > 1 ){
					//
					val imgGeom  = Geometry(imgSize)
					val histgram = Histgram(imgGeom,dataGeom)
					var maxFreq = 0.0
					//
					print("Generating : " + filename ) // do not put newline
					//
					p.generatePoints(numTrajectory)(dropIter,maxIter) {
						(count:Int,p:Vector[Double]) =>
							val freq =	if (count<0){
											val i = count + dropIter
											i.asInstanceOf[Double] / dropIter.asInstanceOf[Double]
										}
										else {
											log(Math.E + count)
										}
							//
							maxFreq = max( histgram.update(p,freq) , maxFreq)
							//
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
							val canvas = new PictureFile(file,imgGeom,imgType,colorBG)
							canvas.paint{ g:Graphics2D =>
								histgram.rendering(samplingDegree){ (ip,avgFreq) =>
									//
									val ratio = avgFreq / maxFreq
									val alpha = pow(ratio,1.0/gammaCorrection)
									//
									val colVector = Vector(alpha,alpha,alpha,1.0)
									//
									g.setColor( colVector )
									g.drawLine( ip.x , ip.y , ip.x , ip.y )
								}
								print(" ... Writing") // do not put newline
							}
							println(" ... Done")
						}
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
