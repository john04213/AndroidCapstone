package com.pnc.customercompanion.core.session

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoCustomer @Inject constructor() {
    val id: Int = 11502
    val name: String = "Jared Peterson"
}
