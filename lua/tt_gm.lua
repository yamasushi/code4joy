require "vector"
require "geometry"
require "chaos_renderer"
require "chaos_system"

require "ds_gumowski_mira"

img_width  = 800
img_height = 800
mu,nu      = 0 , 1 -- -0.485 , 1.0
param_a,param_b = 0.008, 0.05
oval_r = 0.1 -- 5.0

max_iter= 2000
num_traj= 100

header = "A"
pi  = function(t) return math.abs(t) end
psi = function(t) return math.abs(t) end

meta_phi = function(pi,psi)
	return function(t) return pi(t)/(1+psi(t)) end
end

params = {}
table.insert(params,DSGumowskiMira:new("A",0.008,0.05,-0.485,1.0 , pi , psi , meta_phi))
table.insert(params,DSGumowskiMira:new("A",0.008,0.05, 0    ,1.0 , pi , psi , meta_phi))

for i,gm in ipairs(params) do
	print(i,gm)
	chaos_system = ChaosSystem:new(gm,max_iter)
	--print(chaos_system)
	cr = ChaosRenderer:new(chaos_system,num_traj,oval_r)
	--print(cr)
	cr:render(img_width,img_height)
end
