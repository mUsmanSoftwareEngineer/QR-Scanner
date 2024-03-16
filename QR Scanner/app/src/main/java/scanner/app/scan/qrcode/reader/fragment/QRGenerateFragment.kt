package scanner.app.scan.qrcode.reader.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import scanner.app.scan.qrcode.reader.R
import scanner.app.scan.qrcode.reader.activity.GenerateActivity
import scanner.app.scan.qrcode.reader.adapter.BarcodeAdapter
import scanner.app.scan.qrcode.reader.adapter.GenerateAdapter
import scanner.app.scan.qrcode.reader.data.preference.GenerateModel

class QRGenerateFragment : Fragment() {
    var activity: Activity? = null
    var generateModelList: MutableList<GenerateModel>? = null
    var barcodeModelList: MutableList<GenerateModel>? = null
    var generateSocailList: List<Int>? = null
    private var mContext: Context? = null
    private var mRecyclerView: RecyclerView? = null
    private var barcodeRecyclerView: RecyclerView? = null
    private var adapter: GenerateAdapter? = null
    private var barcodeAdapter: BarcodeAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_generate, container, false)
        initView(rootView)
        initFunctionality()
        initListener()
        return rootView
    }

    private fun initVar() {
        activity = requireActivity()
        mContext = (activity as FragmentActivity).applicationContext
        generateModelList = ArrayList()
        barcodeModelList = ArrayList()
        generateSocailList = ArrayList()
    }

    private fun initView(rootView: View) {
        mRecyclerView = rootView.findViewById(R.id.generate_recycler)
        barcodeRecyclerView = rootView.findViewById(R.id.barcode_recycler)
    }

    private fun initFunctionality() {
        val currentFocus = activity!!.currentFocus
        currentFocus?.clearFocus()
        addDataToGenerateAdapter()
        addDataToBarcodeAdapter()
        adapter = GenerateAdapter(mContext, generateModelList)
        mRecyclerView!!.layoutManager = GridLayoutManager(mContext, 3)
        mRecyclerView!!.adapter = adapter
        barcodeAdapter = BarcodeAdapter(mContext, barcodeModelList)
        barcodeRecyclerView!!.layoutManager = GridLayoutManager(mContext, 3)
        barcodeRecyclerView!!.adapter = barcodeAdapter
    }

    private fun addDataToBarcodeAdapter() {
        barcodeModelList!!.add(
            GenerateModel(
                R.drawable.bar_code_icon2,
                BarcodeFormat.CODE_128.toString() + ""
            )
        )
        barcodeModelList!!.add(
            GenerateModel(
                R.drawable.bar_code_icon3,
                BarcodeFormat.CODE_39.toString() + ""
            )
        )
        barcodeModelList!!.add(
            GenerateModel(
                R.drawable.bar_code_icon4,
                BarcodeFormat.CODE_93.toString() + ""
            )
        )
        barcodeModelList!!.add(
            GenerateModel(
                R.drawable.bar_code_icon5,
                BarcodeFormat.CODABAR.toString() + ""
            )
        )
        barcodeModelList!!.add(
            GenerateModel(
                R.drawable.bar_code_icon2,
                BarcodeFormat.ITF.toString() + ""
            )
        )
        barcodeModelList!!.add(
            GenerateModel(
                R.drawable.bar_code_icon3,
                BarcodeFormat.EAN_8.toString() + ""
            )
        )
        barcodeModelList!!.add(
            GenerateModel(
                R.drawable.bar_code_icon4,
                BarcodeFormat.EAN_13.toString() + ""
            )
        )
        barcodeModelList!!.add(
            GenerateModel(
                R.drawable.bar_code_icon5,
                BarcodeFormat.UPC_A.toString() + ""
            )
        )
        barcodeModelList!!.add(
            GenerateModel(
                R.drawable.bar_code_icon2,
                BarcodeFormat.UPC_E.toString() + ""
            )
        )
        barcodeModelList!!.add(GenerateModel(R.drawable.bar_code_icon3, "Product"))
        barcodeModelList!!.add(GenerateModel(R.drawable.bar_code_icon4, "ISBN"))
    }

    private fun addDataToGenerateAdapter() {
        generateModelList!!.add(
            GenerateModel(
                R.drawable.text_scanner,
                activity!!.resources.getString(R.string.txt_generate)
            )
        )
        generateModelList!!.add(
            GenerateModel(
                R.drawable.contacts_scanner,
                activity!!.resources.getString(R.string.contact_generate)
            )
        )
        generateModelList!!.add(
            GenerateModel(
                R.drawable.url_scanner,
                activity!!.resources.getString(R.string.url_generate)
            )
        )
        generateModelList!!.add(
            GenerateModel(
                R.drawable.wifi_scanner,
                activity!!.resources.getString(R.string.wifi_generate)
            )
        )
        generateModelList!!.add(
            GenerateModel(
                R.drawable.email_scanner,
                activity!!.resources.getString(R.string.email_generate)
            )
        )
        generateModelList!!.add(
            GenerateModel(
                R.drawable.sms_scanner,
                activity!!.resources.getString(R.string.sms_generate)
            )
        )
        generateModelList!!.add(
            GenerateModel(
                R.drawable.location_scanner,
                activity!!.resources.getString(R.string.location_generate)
            )
        )
        generateModelList!!.add(
            GenerateModel(
                R.drawable.call_scanner,
                activity!!.resources.getString(R.string.call_generate)
            )
        )
        generateModelList!!.add(
            GenerateModel(
                R.drawable.event_scanner,
                activity!!.resources.getString(R.string.event_generate)
            )
        )
        generateModelList!!.add(
            GenerateModel(
                R.drawable.facebook_icon,
                activity!!.resources.getString(R.string.facebook)
            )
        )
        generateModelList!!.add(
            GenerateModel(
                R.drawable.twitter_icon,
                activity!!.resources.getString(R.string.twitter)
            )
        )
        generateModelList!!.add(
            GenerateModel(
                R.drawable.linkdein_icon,
                activity!!.resources.getString(R.string.linkdein)
            )
        )
        generateModelList!!.add(
            GenerateModel(
                R.drawable.instagram_icon,
                activity!!.resources.getString(R.string.instagram)
            )
        )
        generateModelList!!.add(
            GenerateModel(
                R.drawable.whatsapp_icon,
                activity!!.resources.getString(R.string.whatsapp)
            )
        )
    }

    private fun initListener() {
        adapter!!.setClickListener { position ->
            val intent = Intent(getActivity(), GenerateActivity::class.java)
            intent.putExtra("qr_code_cat", position)
            startActivity(intent)
        }
        barcodeAdapter!!.setClickListener { position ->
            val intent = Intent(getActivity(), GenerateActivity::class.java)
            intent.putExtra("bar_code_cat", position + 2)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
    }
}
