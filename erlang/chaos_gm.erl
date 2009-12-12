-module(chaos_gm).
%% Gumowski-Mira chaos

-include_lib("wx/include/wx.hrl").

-export([start/0]).

start()->
	ImgWidth  = 500 ,
	ImgHeight = 500 ,
	
	OvalR     = 1.0 ,
	NumRing   = 15  ,
	
	NumTrajectory         = 100 ,
	NumPointPerTrajectory = 1000 ,
	%%
	ImgType   = "png"   ,
	FileName  = "a.png" ,
	%%
	bitmap:create_picture_file(
		ImgWidth ,
		ImgHeight,
		1        , %% image depth
		ImgType  ,
		FileName ,
		fun(DC)->
			lists:foreach(
			fun(J)->
				IRing = (J rem NumRing) + 1      ,
				R     = (OvalR * IRing)/NumRing  ,
				Theta = random:uniform() * math:pi() * 2.0 , 
				Pt0   = { R*math:cos( Theta ) , R*math:sin( Theta ) } ,
				%%io:format("~w , ~w , ~w~n",[J,Theta,Pt0]) ,
				%%
				loop(NumPointPerTrajectory , Pt0 , DC,{ImgWidth,ImgHeight})
			end ,
			lists:seq(0,NumTrajectory) )
		end
	).


f_0(A,X)-> 
	X_X = X*X , 
	A * X + ( 2*(1-A)*X_X / (1+X_X) ) .

f_1(A,X) ->
	X_X = X*X ,
	A * X + ( 2*(1-A)*X_X / ((1+X_X)*(1+X_X)) ) .

% FROM HTTP://CODEZINE.JP/ARTICLE/DETAIL/327?P=2
f_2(A,X) ->
	X_X = X*X ,
	A * X + ( 2*(1-A)*X / (1+X_X) ) .

% FROM HTTP://CODEZINE.JP/ARTICLE/DETAIL/327?P=2
f_3(A,X) ->
	X_X = X*X ,
	A * X + ( 2*(1-A) / (1+X_X) ) .

g_0({XA,XB},B,Y) ->
	B *Y + XA * (1- XB*Y*Y)*Y.

loop(I,{X,Y},DC,{ImgWidth,ImgHeight})->
	XA    =  0.0005 ,
	XB    =  1.0    ,
	A     = -0.48   ,
	B     =  0.9924 ,
	F     = fun(T)->f_0(A,T) end , 
	G     = fun(T)->g_0({XA,XB},B,T) end ,
	Param = {F,G},
	U     = F(X) ,
	loop(I,{{X,Y},U},Param,DC,{ImgWidth,ImgHeight}).
	
	
loop(I,{{X,Y},U},Param,DC,{ImgWidth,ImgHeight})->
	
	LimitPositive =  1.0e6 ,
	LimitNegative = -1.0e6 ,
	%%
	Ratio     = 20.0       ,
	Offsetx   = ImgWidth /2 ,
	Offsety   = ImgHeight/2 ,
	%%
	%%io:format("(~p,~p)~n",[X,Y]) ,
	if
		I < 0 -> ok;
		X < LimitNegative -> ok ;
		X > LimitPositive -> ok ;
		Y < LimitNegative -> ok ;
		Y > LimitPositive -> ok ;
		true -> 
			IX = erlang:trunc(               Ratio*X + Offsetx   ),
			IY = erlang:trunc( ImgHeight - ( Ratio*Y + Offsety ) ),
			%%
			%%io:format("-->(~p,~p)~n",[IX,IY]) ,
			if
				(IX >= 0) and (IY >=0) and (IX < ImgWidth) and (IY < ImgHeight) ->
					%%io:format("-->drawPoint~n") ,
					wxDC:drawPoint(DC,{IX,IY}) ;
				true -> ok
			end,
			%%
			loop(I-1 , 
				gm_map(Param,{{X,Y},U}) ,
				Param , 
				DC    ,
				{ImgWidth,ImgHeight})
	end.

gm_map({F,G},{{X,Y},U}) ->
	XX = G(Y) + U ,
	UU = F(XX) ,
	YY = UU - X   ,
	{{XX,YY},UU}.

