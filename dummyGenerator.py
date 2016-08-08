import os
directory = "dummy"
if not os.path.exists(directory):
	os.makedirs(directory)
with open(directory+'/output_file', 'wb') as fout:
    fout.write(os.urandom(1024))