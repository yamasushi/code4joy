// Simple Gumowski-Mira Strange Attractor

import scala.collection.immutable._
import Math.{sin,cos,log,sqrt,abs,exp}

chaosParam.doMain(args)

object chaosParam extends ChaosParameter[DSSimplifiedGumowskiMira]
{
	// eigenvalue ((a,b)(-1,0)) = 1
	def fillParamEP1(name:String ,
				pi     : (Double)=>Double       ,
				psi    : (Double)=>Double       ,
				cRange : Stream[Double] ,
				dRange : Stream[Double] ,
				ovalR  : Double )(map:((Double,Double,Double,Double))=>(Vector[Double])=>Vector[Double] ) : Unit = {
		for ( c<-cRange ; d<-dRange) {
			val par = (0.0,-1.0,c,d)
			add( new DSSimplifiedGumowskiMira( name ,
						pi , psi , par , map(par) ,  numRing,ovalR ) )
		}
		//
		()
	}
	
	// eigenvalue ((a,b)(-1,0)) = -1
	def fillParamEM1(name:String ,
				pi     : (Double)=>Double       ,
				psi    : (Double)=>Double       ,
				cRange : Stream[Double] ,
				dRange : Stream[Double] ,
				ovalR  : Double )(map:((Double,Double,Double,Double))=>(Vector[Double])=>Vector[Double] ) : Unit = {
		for ( c<-cRange ; d<-dRange) {
			val par = (0.0, 1.0,c,d)
			add( new DSSimplifiedGumowskiMira( name ,
						pi , psi , par , map(par) ,  numRing,ovalR )  )
		}
		//
		()
	}
	
	def fillParam(name:String ,
				pi     : (Double)=>Double       ,
				psi    : (Double)=>Double       ,
				aRange : Stream[Double] ,
				bRange : Stream[Double] ,
				cRange : Stream[Double] ,
				dRange : Stream[Double] ,
				ovalR  : Double )(map:((Double,Double,Double,Double))=>(Vector[Double])=>Vector[Double] ) : Unit = {
		for ( a<-aRange ; b<-bRange ; c<-cRange ; d<-dRange) {
			val par = (a,b,c,d)
			add( new DSSimplifiedGumowskiMira( name ,
						pi , psi , par , map(par) ,  numRing,ovalR ) )
		}
		//
		()
	}
	//----------------------------
	def setup() : Unit = {
		import GumowskiMiraPhi._
		
		def mapA(param:(Double,Double,Double,Double)) : (Vector[Double])=>Vector[Double] = {
			val (a,b,c,d) = param
			val ratio = abs(a)+1 //4*abs(phi(1.0)) // experimental scale ratio for width
			
			import Transform._
			val theta = (3*Math.Pi)/4.0
			//
			if (a>= 0) { p => scale(ratio,    1)(rotate(theta)(p) ) }
			else       { p => scale(1    ,ratio)(rotate(theta)(p) ) }
		}
		
		var maps  =Queue[(String,(Double)=>Double,(Double)=>Double)]()
		var params=Queue[(Stream[Double],Stream[Double],Stream[Double],Stream[Double],Double)]()
		
		// maps=maps enqueue("GB"      ,{t=>abs(t)} , {t=>0} )
		// maps=maps enqueue("C"       ,{x=>abs(x*x*x)  },{x=>abs(x*x*x) })
		// maps=maps enqueue("Q"       ,{x=>x*x*x*x     },{x=>x*x*x*x    })
		// maps=maps enqueue("W0"      ,{x=> 1          },psiW )
		// maps=maps enqueue("WR"      ,{x=>sqrt(abs(x))},psiW )
		// maps=maps enqueue("WW"      ,psiW             ,psiW )
		// maps=maps enqueue("_210_124",{x => x*x -2*x +4 } , {x => x*x } )
		// maps=maps enqueue("A10"     ,{x=>x+1         },{x=>abs(x)     })
		// maps=maps enqueue("AA10"    ,{x=>abs(x+1)    },{x=>abs(x)     })
		
		// maps=maps enqueue("AP1AP2_TC"   , {x => cos( x ) } , {x =>abs(x+1)*abs(x+2) } ) // experimental
		
		// maps=maps enqueue("AP1AP2_TS"   , {x => sin( x ) } , {x =>abs(x+1)*abs(x+2) } ) // experimental
		// maps=maps enqueue("AP1AP2_ATS"  , {x => abs(sin( x )) } , {x =>abs(x+1)*abs(x+2) } ) // favorite
		// maps=maps enqueue("AP1AP2_ATSA" , {x => abs(sin( abs(x) )) } , {x =>abs(x+1)*abs(x+2) } ) // favorite
		// maps=maps enqueue("AP1AP2_TSA"  , {x => sin( abs(x) ) } , {x =>abs(x+1)*abs(x+2) } ) // favorite
		
		// maps=maps enqueue("AP1AP2_R"  , {x =>       sqrt(abs(x)) } , {x =>abs(x+1)*abs(x+2) } ) // favorite
		// maps=maps enqueue("AP1AP2_1R" , {x =>x     *sqrt(abs(x)) } , {x =>abs(x+1)*abs(x+2) } ) 
		// maps=maps enqueue("AP1AP2_AR" , {x =>abs(x)*sqrt(abs(x)) } , {x =>abs(x+1)*abs(x+2) } ) 
		
		// _,A        -0.9  , 0.96 ,-4 , 1 : bird
		// WA,A        0.34 , 1    , 1 , 0 : rabbit
		//             0.4  , 1    , 1 , 0
		// A          -1.5  , 1    , 1 , 0 : mickey
		// _11_AP1AM2  0   , 1 , 1 , 0 : doggy face
		// _11_AP1AM2  0.1 , 1 , 1 , 0 : Teddy bear
		maps=maps enqueue("WA"      ,{x=>abs(x)      },psiW )
		maps=maps enqueue("A"       ,{x=>abs(x)      },{x=>abs(x)     })
		// maps=maps enqueue("_"       ,{x=>x*x         },{x=>x*x        })
		// maps=maps enqueue("AP1AP2_0" , {x => 1 } , {x =>abs(x+1)*abs(x+2) } ) // favorite
		// maps=maps enqueue("_11_AP1AM2",{x =>abs(x+1)*abs(x-2) } , {x => x*x } ) // favorite
		
		// maps=maps enqueue("_11_AM1AM2",{x =>abs(x-1)*abs(x-2) } , {x => x*x } )
		// maps=maps enqueue("_11_AP1AP2",{x =>abs(x+1)*abs(x+2) } , {x => x*x } )
		// maps=maps enqueue("_11_AM1AP2",{x =>abs(x-1)*abs(x+2) } , {x => x*x } )
		// maps=maps enqueue("_11_AP1AM2",{x =>abs(x+1)*abs(x-2) } , {x => x*x } )
		
		// maps=maps enqueue("AM1AM2_0" , {x => 1 } , {x =>abs(x-1)*abs(x-2) } )
		// maps=maps enqueue("AP1AP2_0" , {x => 1 } , {x =>abs(x+1)*abs(x+2) } )
		// maps=maps enqueue("AM1AP2_0" , {x => 1 } , {x =>abs(x-1)*abs(x+2) } )
		// maps=maps enqueue("AP1AM2_0" , {x => 1 } , {x =>abs(x+1)*abs(x-2) } )
		
		// maps=maps enqueue("AM1AM2_1" , {x => x } , {x =>abs(x-1)*abs(x-2) } )
		// maps=maps enqueue("AP1AP2_1" , {x => x } , {x =>abs(x+1)*abs(x+2) } )
		// maps=maps enqueue("AM1AP2_1" , {x => x } , {x =>abs(x-1)*abs(x+2) } )
		// maps=maps enqueue("AP1AM2_1" , {x => x } , {x =>abs(x+1)*abs(x-2) } )
		
		// maps=maps enqueue("AM1AM2_2" , {x => x*x } , {x =>abs(x-1)*abs(x-2) } )
		// maps=maps enqueue("AP1AP2_2" , {x => x*x } , {x =>abs(x+1)*abs(x+2) } )
		// maps=maps enqueue("AM1AP2_2" , {x => x*x } , {x =>abs(x-1)*abs(x+2) } )
		// maps=maps enqueue("AP1AM2_2" , {x => x*x } , {x =>abs(x+1)*abs(x-2) } )
		
		// maps=maps enqueue("A111_11_AM1AM2",{x =>abs(x-1)*abs(x-2) } , {x => abs(x*x+x+1) } )
		// maps=maps enqueue("A111_11_AP1AP2",{x =>abs(x+1)*abs(x+2) } , {x => abs(x*x+x+1) } )
		// maps=maps enqueue("A111_11_AM1AP2",{x =>abs(x-1)*abs(x+2) } , {x => abs(x*x+x+1) } )
		// maps=maps enqueue("A111_11_AP1AM2",{x =>abs(x+1)*abs(x-2) } , {x => abs(x*x+x+1) } )
		
		// maps=maps enqueue("_11_M1M2",{x =>(x-1)*(x-2) } , {x => x*x } )
		// maps=maps enqueue("_11_P1P2",{x =>(x+1)*(x+2) } , {x => x*x } )
		// maps=maps enqueue("_11_M1P2",{x =>(x-1)*(x+2) } , {x => x*x } )
		// maps=maps enqueue("_11_P1M2",{x =>(x+1)*(x-2) } , {x => x*x } )
		
		// maps=maps enqueue("A11_M1M2",{x =>(x-1)*(x-2) } , {x => abs(x) } )
		// maps=maps enqueue("A11_P1P2",{x =>(x+1)*(x+2) } , {x => abs(x) } )
		// maps=maps enqueue("A11_M1P2",{x =>(x-1)*(x+2) } , {x => abs(x) } )
		// maps=maps enqueue("A11_P1M2",{x =>(x+1)*(x-2) } , {x => abs(x) } )
		
		// params=params enqueue (
					// ParamRange.neighbor( -0.9   , 0.01 , 0 ) , 
					// ParamRange.neighbor(  0.96  , 0.01 , 0 ) , 
					// ParamRange.neighbor( -4.0   , 0.01 , 0 ) , 
					// ParamRange.neighbor(  1.0   , 0.01 , 0 ) , 
					// 1.0 )
					
		params=params enqueue (
					ParamRange.neighbor(  0.34  , 0.01 , 10 ) , 
					ParamRange.neighbor(  1.0   , 0.01 ,  0 ) , 
					ParamRange.neighbor(  1.0   , 0.01 ,  0 ) , 
					ParamRange.neighbor(  0.0   , 0.01 ,  0 ) , 
					1.0 )
					
		params=params enqueue (
					ParamRange.neighbor( -0.34  , 0.01 , 10 ) , 
					ParamRange.neighbor(  1.0   , 0.01 ,  0 ) , 
					ParamRange.neighbor(  1.0   , 0.01 ,  0 ) , 
					ParamRange.neighbor(  0.0   , 0.01 ,  0 ) , 
					1.0 )
					
		// params=params enqueue (
					// ParamRange.neighbor( 0   , 0.01 , 0 ) , 
					// ParamRange.neighbor( 1   , 0.01 , 0 ) , 
					// ParamRange.neighbor( 1   , 0.01 , 0 ) , 
					// ParamRange.neighbor( 0   , 0.01 , 0 ) , 
					// 1.0 )
		
		// params=params enqueue (
					// ParamRange.neighbor( 0.1 , 0.01 , 0 ) , 
					// ParamRange.neighbor( 1   , 0.01 , 0 ) , 
					// ParamRange.neighbor( 1   , 0.01 , 0 ) , 
					// ParamRange.neighbor( 0   , 0.01 , 0 ) , 
					// 1.0 )
		
		// for(m<-maps;p<-params) {
			// fillParam( "RAW-"+m._1 , m._2,m._3 , p._1,p._2,p._3,p._4,p._5){ _ =>{p=>p}}
		// }
		
		for(m<-maps;p<-params) {
			fillParam( m._1 , m._2,m._3 , p._1,p._2,p._3,p._4,p._5)(mapA)
		}
	}
}

