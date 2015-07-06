Vagrant.configure(2) do |config|
	config.vm.box = 'ubuntu.lts.64'
	# a few comments...
	config.vm.box_url = 'http://files.vagrantup.com/precise64.box'
	config.vm.provision "shell", path: "Vagrantscript"
	config.vm.synced_folder ".", "/vagrant"
end
