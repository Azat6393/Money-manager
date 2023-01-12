package com.woynex.parasayar.core.utils

sealed class FeeState{
    object NewFee: FeeState()
    object DeleteFee: FeeState()
    object UpdateFee: FeeState()
    object EmptyFee: FeeState()
}
