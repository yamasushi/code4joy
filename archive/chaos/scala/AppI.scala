// Ikeda Strange Attractor
import scala.collection.immutable._

chaosParam.doMain(args)

object chaosParam extends ChaosParameter[DSIkeda]
{
	def setup() : Unit = {
		var paramsAB = Queue[(Stream[Double],Stream[Double])]()
		var paramsKP = Queue[(Stream[Double],Stream[Double])]()
		//
		paramsAB=paramsAB enqueue (
						ParamRange.neighbor(0.9,0.01,20) ,
						ParamRange.neighbor(0.9,0.01,0) )
		
		paramsKP=paramsKP enqueue (
						ParamRange.neighbor(0.4,0.01,0) ,
						ParamRange.neighbor(6.0,0.01,0) )
		
		paramsKP=paramsKP enqueue (
						ParamRange.neighbor(0.4,0.01,0) ,
						ParamRange.neighbor(7.7,0.01,0) )
		
		for( abRange<-paramsAB ; kpRange <-paramsKP ){
			for( a<-abRange._1 ; b<-abRange._2 ; k<-kpRange._1 ; p<-kpRange._2 ) {
				add( new DSIkeda(
							(a,b),(k,p), numRing, 0.1  ) )
			}
		}
		
	}
}
