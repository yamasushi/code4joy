-module(chaos_henon).
%% henon chaos

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
				loop(NumPointPerTrajectory,Pt0,DC,{ImgWidth,ImgHeight})
			end ,
			lists:seq(0,NumTrajectory) )
		end
	).

h_sq(X) -> X*X.

h_cu(X) -> X*X*X.

h_si(X) -> 0.8*math:sin(X).

h_in(X) -> 0.1 / X.

loop(I,{X,Y},DC,{ImgWidth,ImgHeight})->
	Alpha = 0.47 * math:pi() ,
	% 0.07
	% 0.30
	% 0.39
	% 0.47
	% 0.61
	% 0.84
	Param = {fun(T)->h_sq(T) end ,math:cos(Alpha),math:sin(Alpha)} ,
	
	LimitPositive =  1.0e6 ,
	LimitNegative = -1.0e6 ,
	%%
	Ratio     = 300.0       ,
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
			loop(I-1,henon_map(Param,{X,Y}),DC,{ImgWidth,ImgHeight})
	end.

henon_map({Func,CosAlpha,SinAlpha},{X,Y}) ->
	Temp  = Y - Func(X) ,
	{	X*CosAlpha - Temp*SinAlpha ,
		X*SinAlpha + Temp*CosAlpha } .

