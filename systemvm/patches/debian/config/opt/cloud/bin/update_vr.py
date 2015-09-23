#!/usr/bin/python

from ConfigParser import SafeConfigParser;
import subprocess;
import shlex;

manifest_config = SafeConfigParser();

def install_rpms():
	global manifest_config;
	file_no = 1;
	while (file_no < len(manifest_config.options("packages"))):
		pkg = manifest_config.get("packages", "FILE"+str(file_no));
		print "Installing " + pkg;
		subprocess.call(str("rpm -Uvh "+pkg).split());
		file_no = file_no+1;

def exec_commands(section):
	global manifest_config;
	cmd_no = 1;
	while (cmd_no < len(manifest_config.options(section))):
		cmd = manifest_config.get(section, "CMD"+str(cmd_no));
		subprocess.call(shlex.split(cmd));
		cmd_no = cmd_no+1;

manifest_config.read("manifest.nfo");
prev_ver = manifest_config.get("general", "PREV_VERSION");
next_ver = manifest_config.get("general", "NEXT_VERSION");
print "Updating from "+prev_ver+" to "+next_ver;
exec_commands("pre_install");
install_rpms();
exec_commands("post_install");
