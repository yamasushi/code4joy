// Chaos Game
import scala.collection.immutable._
import Math.{sin,cos,log,sqrt,abs,exp}

chaosParam.doMain(args)

object chaosParam extends ChaosParameter[DSGumowskiMira]
{
	def setup() : Unit = {
		import GumowskiMiraPhi._
		
		var sys = new List[ChaosSystem]
		sys = (new DSGumowskiMira("",phi(psiQuadratic _ ) ,
									( 0.008, 0.05 ) , ( -0.475 , 1.0 ) , 1 , 1 )).chaosSystem :: sys
		
		new ChaosSystem{
			override def mapDifference(p:Vector[Double]) : Vector[Double] = {
				
			}
		} :: sys
		
		add( new DSChaosGame( "" , sys , 1 , 1 ) 
		
		()
	}
}

