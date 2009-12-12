-module(chaos_ikeda).
%% ikeda chaos

-include_lib("wx/include/wx.hrl").

-export([start/0]).

start()->
	ImgWidth  = 500 ,
	ImgHeight = 500 ,
	
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
				Theta = random:uniform() * math:pi() * 2.0 , 
				Pt0   = { math:cos( Theta ) , math:sin( Theta ) } ,
				%%io:format("~w , ~w , ~w~n",[J,Theta,Pt0]) ,
				%%
				loop(NumPointPerTrajectory,Pt0,DC,{ImgWidth,ImgHeight})
			end ,
			lists:seq(0,NumTrajectory) )
		end
	).

loop(I,{X,Y},DC,{ImgWidth,ImgHeight})->
	Param = {1.0 , 0.9 , 0.4 , 6.0 } ,
	LimitPositive =  1.0e6 ,
	LimitNegative = -1.0e6 ,
	%%
	Ratio     = 100.0       ,
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
			loop(I-1,ikeda_map(Param,{X,Y}),DC,{ImgWidth,ImgHeight})
	end.

ikeda_map({A,B,K,P},{X,Y}) ->
	TN    = K - P/(1.0 + (X*X + Y*Y) ) ,
	SinTN = math:sin(TN) ,
	CosTN = math:cos(TN) ,
	{	B * ( X*CosTN - Y*SinTN ) + A , 
		B * ( X*SinTN + Y*CosTN )     }.
