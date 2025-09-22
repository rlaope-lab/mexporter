package lab.monilabexporterex.cmd

interface CmdInterface {
    fun getTcpConnections(): Int

    fun getTcpEstablished(): Int

    fun getOpenSockets(): Int
}
