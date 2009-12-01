// Chaos Game
import scala.collection.immutable._
import Math.{sin,cos,log,sqrt,abs,exp}

chaosParam.doMain(args)

object chaosParam extends ChaosParameter[DSChaosGame]
{
	def setup() : Unit = {
		import GumowskiMiraPhi._
		
		var sys:List[ChaosSystem] = Nil
		sys = (new DSGumowskiMira("",phi(psiQuadratic _ ) ,
									( 0.008, 0.05 ) , ( -0.475 , 1.0 ) , 1 , 1 )).chaosSystem :: sys
		sys = (new DSGumowskiMira("",phi(psiQuadratic _ ) ,
									( 0.008, 0.05 ) , ( -0.47  , 0.9 ) , 1 , 1 )).chaosSystem :: sys
		
		add( new DSChaosGame( "" , sys , 1 , 1 ) ) 
		
		()
	}
}

