package com.example.karnaughmap.main

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.example.karnaughmap.R
import com.example.karnaughmap.databinding.KmapMainFragmentBinding
import com.example.karnaughmap.Kmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class KmapMainFragment : Fragment() {

    private lateinit var kmapMainViewModel: KmapMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<KmapMainFragmentBinding>(
            inflater,
            R.layout.kmap_main_fragment,
            container,
            false
        )
        val args: KmapMainFragmentArgs by navArgs()
        val application = requireNotNull(this.activity).application

        val factory = KmapMainViewModelFactory(application, args.userId)
        kmapMainViewModel = ViewModelProvider(this, factory).get(KmapMainViewModel::class.java)

        binding.lifecycleOwner = this
        binding.kmapMainViewModel = kmapMainViewModel

        kmapMainViewModel.kmap.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                buildKmapTable(it, binding.kmapTable)
            }
        })

        kmapMainViewModel.isExpressionLongValid.observe(viewLifecycleOwner, Observer {
            if (!it) {
                binding.expressionEdit.error = "Выражение введено некорректно"
            }
        })

        kmapMainViewModel.eventMapSaved.observe(viewLifecycleOwner, Observer {
            if (it) {
                val uri: Uri? = saveImage(binding.kmapTable)
                if (uri != null) {
                    Log.i("KmapMainFragment", "Map saved in: $uri")
                    this.findNavController().navigate(
                        KmapMainFragmentDirections.actionKmapMainFragmentToMenuFragment(
                            args.userId
                        )
                    )
                    kmapMainViewModel.saveMapComplete(uri)
                }
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(
            item
        )
    }

    private fun saveImage(view: View): Uri? {
        var uri: Uri? = null
        val bitmap: Bitmap = view.drawToBitmap()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "$timeStamp.png"
        val fileDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        try {
            val file = File(fileDir, fileName)
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
            uri = Uri.parse(file.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return uri
    }

    private fun buildKmapTable(kmap: Kmap, table: ViewGroup) {
        val alphabet = arrayOf('a', 'b', 'c', 'd', 'e', 'f')
        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        ).apply {
            column = kmap.cols+1
        }

        table.removeAllViews()

        var newRow = TableRow(context)

        val divide: Int = kmap.varsCount / 2 // Разбиение переменных для вывода заголовков стол

        // Создание заголовка таблицы
        var title = ""
        for (i in 0 until kmap.varsCount) {
            title += alphabet[i]
        }
        title = title.substring(divide, title.length) + "\n" + title.substring(0, divide)
        var newText = TextView(context)
        newText.text = title
        newRow.addView(newText, layoutParams)

        // Заголовки столбцов
        for (i in 0 until kmap.cols) {
            title = ""
            for (j in divide until kmap.varsCount) {
                title += if (kmap.grayCodeTable[i][j]) "1" else "0"
            }
            newText = TextView(context)
            newText.setPadding(10, 5, 10, 5)
            newText.text = title
            newRow.addView(newText, layoutParams)
        }
        table.addView(newRow)

        // Заполнение основной части таблицы
        for (i in 0 until kmap.rows) {
            // Заголовки для строк
            title = ""
            for (k in 0 until divide) {
                title += if (kmap.grayCodeTable[i * kmap.cols][k]) "1" else "0"
            }
            newText = TextView(context)
            newText.setPadding(10, 5, 10, 5)
            newText.text = title
            newRow = TableRow(context)
            newRow.addView(newText, layoutParams)

            // Значения ячеек
            if (i % 2 == 0) {
                for (j in 0 until kmap.cols) {
                    newText = TextView(context)
                    newText.setPadding(10, 5, 10, 5)
                    newText.text = if (kmap.values[i * kmap.cols + j]) "1" else "0"
                    newRow.addView(newText, layoutParams)
                }
            } else {
                for (j in kmap.cols-1 downTo 0) {
                    newText = TextView(context)
                    newText.setPadding(10, 5, 10, 5)
                    newText.text = if (kmap.values[i * kmap.cols + j]) "1" else "0"
                    newRow.addView(newText, layoutParams)
                }
            }
            table.addView(newRow)
        }
    }

}