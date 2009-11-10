// gumowski-mira chaos system
// 2009-09-11
// 10:15 2009/10/13 simplyfied
// see:
//   http://www.scipress.org/journals/forma/pdf/1502/15020121.pdf
//   http://math.cmaisonneuve.qc.ca/alevesque/chaos_fract/Attracteurs/Attracteurs.html
// see also:
//   http://codezine.jp/article/detail/327?p=2
//   http://www.ny.airnet.ne.jp/satoh/chaos.htm
//   http://demonstrations.wolfram.com/StrangeAttractorOfGumowskiMira/
//
//
import Math.{log,sin,cos,sqrt,abs,exp,pow}

object GumowskiMiraPhi
{
	def phi(pi:(Double)=>Double , psi:(Double)=>Double)(x:Double) : Double = {
		val psiX = psi(x)
		val piX  = pi (x)
		piX / ( 1.0 + psiX )
	}
	//---------------------------------------------
	def phi   (psi:(Double)=>Double)(x:Double) : Double = phi( psi             , psi )(x)
	def phiCZ0(psi:(Double)=>Double)(x:Double) : Double = phi( { x=> 1       } , psi )(x)
	def phiCZ1(psi:(Double)=>Double)(x:Double) : Double = phi( { x=> x       } , psi )(x)
	def phiCZ2(psi:(Double)=>Double)(x:Double) : Double = phi( { x=> x*x     } , psi )(x)
	def phiCZ3(psi:(Double)=>Double)(x:Double) : Double = phi( { x=> x*x*x   } , psi )(x)
	def phiCZ4(psi:(Double)=>Double)(x:Double) : Double = phi( { x=> x*x*x*x } , psi )(x)
	//---------------------------------------------
	// typo. of wolfram
	// any x , psi(x) >= 0
	// psi( 0) ==  0
	// psi( 1) == psi(-1) == 3
	// typo. of wolfram
	def psiW (x:Double) = { val xx=x*x    ;  xx*(xx + 2) }
	def psiWA(x:Double) = { val xx=abs(x) ;  xx*(xx + 2) }
	def psiWR(x:Double) = { val xx=abs(x) ;  2*sqrt(xx) + xx }
	//---------------------------------------------
	// any x , psi(x) >= 0
	// psi(0) == 0
	// psi(-1) == psi(1) == 1
	def psiQuadratic(x:Double) = { x*x           }
	def psiAbs      (x:Double) = { abs(x)        }
	def psiSqrt     (x:Double) = { sqrt(abs(x))  } 
	def psiQuartic  (x:Double) = { x*x*x*x       }
	def psiCubic    (x:Double) = { abs(x*x*x)    }
	def psiPow      (x:Double) = { pow(2,x*x)-1    }
	def psiPowAbs   (x:Double) = { pow(2,abs(x))-1 }
	def psiTrig     (x:Double) = { 1-cos(0.5*Math.Pi*x) }
	def psiLog      (x:Double) = { log(1 +    x*x*(Math.E-1) ) }
	def psiLogAbs   (x:Double) = { log(1 + abs(x)*(Math.E-1) ) }
	def psiCircle   (x:Double) = { abs(1 - sqrt( abs(1-x*x) ) ) }
	//---------------------------------------------
	def gmPhi       (x:Double) = phi   (psiQuadratic _ )(x) // standard phi : Double ---> Double = [0,1)  
	def gmPhiCZ0    (x:Double) = phiCZ0(psiQuadratic _ )(x) // codezine extention
	def gmPhiCZ1    (x:Double) = phiCZ1(psiQuadratic _ )(x) // codezine extention 
	//---------------------------------------------
	def gmPhiW      (x:Double) = phi   (psiQuadratic _ , psiW _ )(x) // typo. of wolfram 
	//---------------------------------------------
}
