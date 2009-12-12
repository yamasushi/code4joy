-module(chaos_sprott).
% sprott attractor
% output to a.png
% identify attractor as 12 charactor string
% as FIRCDERRPVLD
%
% for more detail , see :
%   http://sprott.physics.wisc.edu/pubs/paper203.htm

-include_lib("wx/include/wx.hrl").

-export([start/0]).

dict_char2coef()->dict:from_list([
	{$A,-1.2},
	{$B,-1.1},
	{$C,-1.0},
	{$D,-0.9},
	{$E,-0.8},
	{$F,-0.7},
	{$G,-0.6},
	{$H,-0.5},
	{$I,-0.4},
	{$J,-0.3},
	{$K,-0.2},
	{$L,-0.1},
	{$M, 0.0}, % <---Zero
	{$N,+0.1},
	{$O,+0.2},
	{$P,+0.3},
	{$Q,+0.4},
	{$R,+0.5},
	{$S,+0.6},
	{$T,+0.7},
	{$U,+0.8},
	{$V,+0.9},
	{$W,+1.0},
	{$X,+1.1},
	{$Y,+1.2}]).

param_coef(SprottName)->
	Dict = dict_char2coef() ,
	Coef = lists:map(fun(Ch) -> dict:fetch(Ch,Dict) end , SprottName),
	{Coef,array:from_list( Coef )}.


start()->
	{Coef,Param} = param_coef("FIRCDERRPVLD") , % 12charactors signature of system 
	%"AMTMNQQXUYGA"
	%"CVQKGHQTPNTE"
	io:format("~w ~n",[Coef]),
	
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
				loop(NumPointPerTrajectory , Pt0 , Param , DC,{ImgWidth,ImgHeight})
			end ,
			lists:seq(0,NumTrajectory) )
		end
	).

loop(I,{X,Y},Param,DC,{ImgWidth,ImgHeight})->
	%
	LimitPositive =  1.0e6 ,
	LimitNegative = -1.0e6 ,
	%%
	Ratio     = 200.0       ,
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
				sprott_map(Param,{X,Y}) ,
				Param , 
				DC    ,
				{ImgWidth,ImgHeight})
	end.

sprott_map(A,{X,Y}) ->
	X_X = X*X ,
	Y_Y = Y*Y ,
	X_Y = X*Y ,
	{	array:get( 0,A)     +  
		array:get( 1,A)*X   + 
		array:get( 2,A)*X_X + 
		array:get( 3,A)*X_Y + 
		array:get( 4,A)*Y   + 
		array:get( 5,A)*Y_Y 
	,
		array:get( 6,A)     + 
		array:get( 7,A)*X   + 
		array:get( 8,A)*X_X + 
		array:get( 9,A)*X_Y + 
		array:get(10,A)*Y   + 
		array:get(11,A)*Y_Y }.
