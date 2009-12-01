// Chaos Game
import scala.collection.immutable._
import Math.{sin,cos,log,sqrt,abs,exp}

chaosParam.doMain(args)

object chaosParam extends ChaosParameter[DSChaosGame]
{
	def setup() : Unit = {
		import GumowskiMiraPhi._
		
		var sys:List[ChaosSystem] = Nil
		sys = (new DSGumowskiMira("",phi(psiAbs _ ) ,
									( 0.008, 0.05 ) , ( -0.475 , 1.0 ) , 1 , 1 )).chaosSystem :: sys
		sys = (new DSGumowskiMira("",phi(psiAbs _ ) ,
									( 0.008, 0.04 ) , ( -0.475 , 1.0 ) , 1 , 1 )).chaosSystem :: sys
		sys = (new DSGumowskiMira("",phi(psiAbs _ ) ,
									( 0.008, 0.03 ) , ( -0.475 , 1.0 ) , 1 , 1 )).chaosSystem :: sys
		sys = (new DSGumowskiMira("",phi(psiAbs _ ) ,
									( 0.008, 0.02 ) , ( -0.475 , 1.0 ) , 1 , 1 )).chaosSystem :: sys
		sys = (new DSGumowskiMira("",phi(psiAbs _ ) ,
									( 0.008, 0.01 ) , ( -0.475 , 1.0 ) , 1 , 1 )).chaosSystem :: sys
		
		sys = (new DSGumowskiMira("",phi(psiAbs _ ) ,
									( 0.008, 0.05 ) , ( -0.475 , 1.0 ) , 1 , 1 )).chaosSystem :: sys
		sys = (new DSGumowskiMira("",phi(psiAbs _ ) ,
									( 0.007, 0.05 ) , ( -0.475 , 1.0 ) , 1 , 1 )).chaosSystem :: sys
		sys = (new DSGumowskiMira("",phi(psiAbs _ ) ,
									( 0.006, 0.05 ) , ( -0.475 , 1.0 ) , 1 , 1 )).chaosSystem :: sys
		sys = (new DSGumowskiMira("",phi(psiAbs _ ) ,
									( 0.005, 0.05 ) , ( -0.475 , 1.0 ) , 1 , 1 )).chaosSystem :: sys
		sys = (new DSGumowskiMira("",phi(psiAbs _ ) ,
									( 0.004, 0.05 ) , ( -0.475 , 1.0 ) , 1 , 1 )).chaosSystem :: sys
		
		add( new DSChaosGame( "" , sys , 1 , 1 ) ) 
		
		()
	}
}

